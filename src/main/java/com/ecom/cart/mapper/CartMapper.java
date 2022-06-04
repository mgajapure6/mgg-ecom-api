package com.ecom.cart.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.ecom.cart.dto.CartProductDTO;
import com.ecom.cart.dto.CartRequestDTO;
import com.ecom.cart.dto.CartResponseDTO;
import com.ecom.cart.model.Cart;
import com.ecom.cart.model.CartProduct;
import com.ecom.product.dto.ProductDTO;

@Service
public class CartMapper {

	public static Cart mapToCart(@Valid CartRequestDTO cartRequest) {
		Cart cart = new Cart();
		cart.setId(cartRequest.getId());
		cart.setProducts(cartRequest.getProducts().stream().map(p -> mapToCartProduct(p)).collect(Collectors.toSet()));
		return cart;
	}

	public static CartResponseDTO mapToCartResponseDTO(Cart newCart, Set<ProductDTO> products) {
		CartResponseDTO cartDTO = new CartResponseDTO();
		cartDTO.setId(newCart.getId());
		cartDTO.setProducts(products);
		return cartDTO;
	}

	public static CartProduct mapToCartProduct(CartProductDTO cartProductDTO) {
		return new CartProduct(cartProductDTO.getId(), cartProductDTO.getProductId(),
				cartProductDTO.getProductQuantity().doubleValue());

	}

}
