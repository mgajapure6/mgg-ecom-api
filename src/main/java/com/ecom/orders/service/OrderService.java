package com.ecom.orders.service;

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
import com.ecom.orders.dto.OrderRequestDTO;
import com.ecom.orders.dto.OrderResponseDTO;
import com.ecom.orders.mapper.OrderMapper;
import com.ecom.orders.model.Order;
import com.ecom.orders.model.OrderProduct;
import com.ecom.orders.repository.OrderProductRepository;
import com.ecom.orders.repository.OrderRepository;
import com.ecom.product.mapper.ProductMapper;
import com.ecom.product.model.Product;
import com.ecom.product.repository.ProductRepository;
import com.ecom.user.model.RoleName;
import com.ecom.user.model.User;
import com.ecom.user.repository.UserRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	public PagedResponse<OrderResponseDTO> getAllOrder(Integer page, Integer size) {
		AppUtil.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstant.CREATED_AT);
		Page<Order> orders = orderRepository.findAll(pageable);
		List<OrderResponseDTO> list = new ArrayList<>();
		if (orders.getNumberOfElements() != 0) {
			List<Order> ordrs = orders.getContent();
			for (Order order : ordrs) {
				List<Long> productIds = order.getProducts().stream().mapToLong(o -> o.getProductId()).boxed()
						.collect(Collectors.toList());
				List<Product> products = productRepository.findByIdIn(productIds);
				OrderResponseDTO orderResponseDTO = OrderMapper.mapToOrderResponseDTO(order,
						products.stream().map(p -> ProductMapper.mapProductToProductResponseDTO(p)).collect(Collectors.toSet()));
				list.add(orderResponseDTO);
			}
		} else {
			list = Collections.emptyList();
		}
		return new PagedResponse<>(list, orders.getNumber(), orders.getSize(), orders.getTotalElements(),
				orders.getTotalPages(), orders.isLast());
	}

	public OrderResponseDTO addOrder(@Valid OrderRequestDTO orderRequest, UserPrincipal currentUser) {
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER, AppConstant.ID, 1L));
		if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString()))
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

			Order order = OrderMapper.mapToOrder(orderRequest);
			order.setUser(user);
			Order newOrder = orderRepository.save(order);

			for (OrderProduct op : newOrder.getProducts()) {
				op.setOrder(newOrder);
				orderProductRepository.save(op);
			}

			List<Long> productIds = newOrder.getProducts().stream().mapToLong(o -> o.getProductId()).boxed()
					.collect(Collectors.toList());
			List<Product> products = productRepository.findByIdIn(productIds);

			OrderResponseDTO orderResponseDTO = OrderMapper.mapToOrderResponseDTO(newOrder,
					products.stream().map(p -> ProductMapper.mapProductToProductResponseDTO(p)).collect(Collectors.toSet()));

			return orderResponseDTO;
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

	public OrderResponseDTO getOrder(Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ORDER, AppConstant.ID, id));
		List<Long> productIds = order.getProducts().stream().mapToLong(o -> o.getProductId()).boxed()
				.collect(Collectors.toList());
		List<Product> products = productRepository.findByIdIn(productIds);

		OrderResponseDTO orderResponseDTO = OrderMapper.mapToOrderResponseDTO(order,
				products.stream().map(p -> ProductMapper.mapProductToProductResponseDTO(p)).collect(Collectors.toSet()));
		return orderResponseDTO;
	}

	public OrderResponseDTO updateOrder(Long id, @Valid OrderRequestDTO updtOrderRequest, UserPrincipal currentUser) {
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER, AppConstant.ID, 1L));

		if (updtOrderRequest.getUserId().equals(user.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString()))) {
			Order order = orderRepository.findById(updtOrderRequest.getId()).orElseThrow(
					() -> new ResourceNotFoundException(AppConstant.ORDER, AppConstant.ID, updtOrderRequest.getId()));

			Set<OrderProduct> oldOrderProducts = new HashSet<>(orderProductRepository.findByOrder(order));
			order.removeAllOrderProduct(oldOrderProducts);

			order = OrderMapper.mapToOrder(updtOrderRequest);
			order.setId(updtOrderRequest.getId());
			order.setUser(user);

			for (OrderProduct op : order.getProducts()) {
				op.setOrder(order);
				orderProductRepository.save(op);
			}

			oldOrderProducts.stream().filter(oop -> oop.getOrder() == null)
					.forEach(oop -> orderProductRepository.delete(oop));

			order = orderRepository.save(order);

			List<Long> productIds = order.getProducts().stream().mapToLong(o -> o.getProductId()).boxed()
					.collect(Collectors.toList());
			List<Product> products = productRepository.findByIdIn(productIds);

			OrderResponseDTO orderResponseDTO = OrderMapper.mapToOrderResponseDTO(order,
					products.stream().map(p -> ProductMapper.mapProductToProductResponseDTO(p)).collect(Collectors.toSet()));
			return orderResponseDTO;
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

	public ApiResponse deleteOrder(Long id, UserPrincipal currentUser) {
		if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_VENDOR.toString()))) {
			orderRepository.deleteById(id);
			return new ApiResponse(Boolean.TRUE, AppMessages.CATEGORY_DELETE_SUCCESS);
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

}
