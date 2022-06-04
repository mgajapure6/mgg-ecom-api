package com.ecom.cart.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.ecom.app.exceptions.ResourceNotFoundException;
import com.ecom.app.exceptions.UnauthorizedException;
import com.ecom.app.payload.ApiResponse;
import com.ecom.app.payload.PagedResponse;
import com.ecom.app.security.UserPrincipal;
import com.ecom.app.utils.AppConstant;
import com.ecom.app.utils.AppMessages;
import com.ecom.app.utils.AppUtil;
import com.ecom.cart.dto.CartRequestDTO;
import com.ecom.cart.dto.CartResponseDTO;
import com.ecom.cart.mapper.CartMapper;
import com.ecom.cart.model.Cart;
import com.ecom.cart.model.CartProduct;
import com.ecom.cart.repository.CartProductRepository;
import com.ecom.cart.repository.CartRepository;
import com.ecom.product.mapper.ProductMapper;
import com.ecom.product.model.Product;
import com.ecom.product.repository.ProductRepository;
import com.ecom.user.model.RoleName;
import com.ecom.user.model.User;
import com.ecom.user.repository.UserRepository;

@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartProductRepository cartProductRepository;

	public PagedResponse<CartResponseDTO> getAllCart(Integer page, Integer size) {
		AppUtil.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstant.CREATED_AT);
		Page<Cart> carts = cartRepository.findAll(pageable);
		List<CartResponseDTO> list = new ArrayList<>();
		if (carts.getNumberOfElements() != 0) {
			List<Cart> ordrs = carts.getContent();
			for (Cart cart : ordrs) {
				List<Long> productIds = cart.getProducts().stream().mapToLong(o -> o.getProductId()).boxed()
						.collect(Collectors.toList());
				List<Product> products = productRepository.findByIdIn(productIds);
				CartResponseDTO cartResponseDTO = CartMapper.mapToCartResponseDTO(cart,
						products.stream().map(p -> ProductMapper.mapToProductDTO(p)).collect(Collectors.toSet()));
				list.add(cartResponseDTO);
			}
		} else {
			list = Collections.emptyList();
		}
		return new PagedResponse<>(list, carts.getNumber(), carts.getSize(), carts.getTotalElements(),
				carts.getTotalPages(), carts.isLast());
	}

	public CartResponseDTO addCart(@Valid CartRequestDTO cartRequest, UserPrincipal currentUser) {
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER, AppConstant.ID, 1L));
		if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString()))
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

			Cart cart = CartMapper.mapToCart(cartRequest);
			cart.setUser(user);
			Cart newCart = cartRepository.save(cart);

			for (CartProduct op : newCart.getProducts()) {
				op.setCart(newCart);
				cartProductRepository.save(op);
			}

			List<Long> productIds = newCart.getProducts().stream().mapToLong(o -> o.getProductId()).boxed()
					.collect(Collectors.toList());
			List<Product> products = productRepository.findByIdIn(productIds);

			CartResponseDTO cartResponseDTO = CartMapper.mapToCartResponseDTO(newCart,
					products.stream().map(p -> ProductMapper.mapToProductDTO(p)).collect(Collectors.toSet()));

			return cartResponseDTO;
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

	public CartResponseDTO getCart(Long id) {
		Cart cart = cartRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ORDER, AppConstant.ID, id));
		List<Long> productIds = cart.getProducts().stream().mapToLong(o -> o.getProductId()).boxed()
				.collect(Collectors.toList());
		List<Product> products = productRepository.findByIdIn(productIds);

		CartResponseDTO cartResponseDTO = CartMapper.mapToCartResponseDTO(cart,
				products.stream().map(p -> ProductMapper.mapToProductDTO(p)).collect(Collectors.toSet()));
		return cartResponseDTO;
	}

	public CartResponseDTO updateCart(Long id, @Valid CartRequestDTO updtCartRequest, UserPrincipal currentUser) {
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER, AppConstant.ID, 1L));

		if (updtCartRequest.getUserId().equals(user.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString()))) {
			Cart cart = cartRepository.findById(updtCartRequest.getId()).orElseThrow(
					() -> new ResourceNotFoundException(AppConstant.ORDER, AppConstant.ID, updtCartRequest.getId()));

			Set<CartProduct> oldCartProducts = new HashSet<>(cartProductRepository.findByCart(cart));
			cart.removeAllCartProducts(oldCartProducts);

			cart = CartMapper.mapToCart(updtCartRequest);
			cart.setId(updtCartRequest.getId());
			cart.setUser(user);

			for (CartProduct op : cart.getProducts()) {
				op.setCart(cart);
				cartProductRepository.save(op);
			}

			oldCartProducts.stream().filter(oop -> oop.getCart() == null)
					.forEach(oop -> cartProductRepository.delete(oop));

			cart = cartRepository.save(cart);

			List<Long> productIds = cart.getProducts().stream().mapToLong(o -> o.getProductId()).boxed()
					.collect(Collectors.toList());
			List<Product> products = productRepository.findByIdIn(productIds);

			CartResponseDTO cartResponseDTO = CartMapper.mapToCartResponseDTO(cart,
					products.stream().map(p -> ProductMapper.mapToProductDTO(p)).collect(Collectors.toSet()));
			return cartResponseDTO;
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

	public ApiResponse deleteCart(Long id, UserPrincipal currentUser) {
		if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_VENDOR.toString()))) {
			cartRepository.deleteById(id);
			return new ApiResponse(Boolean.TRUE, AppMessages.CATEGORY_DELETE_SUCCESS);
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

	public CartResponseDTO getCartByUser(UserPrincipal currentUser) {
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER, AppConstant.ID, 1L));
		Cart cart = cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CART, AppConstant.ID, currentUser.getId()));
		List<Long> productIds = cart.getProducts().stream().mapToLong(o -> o.getProductId()).boxed()
				.collect(Collectors.toList());
		List<Product> products = productRepository.findByIdIn(productIds);

		CartResponseDTO cartResponseDTO = CartMapper.mapToCartResponseDTO(cart,
				products.stream().map(p -> ProductMapper.mapToProductDTO(p)).collect(Collectors.toSet()));
		return cartResponseDTO;
	}

}
