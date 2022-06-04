package com.ecom.cart.dto;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CartRequestDTO {

	private Long id;

	@NotNull
	@Size(min = 1, message = "Cart must contain atleast one product")
	private Set<CartProductDTO> products;

	@NotNull
	private Long userId;

	public CartRequestDTO() {
		super();
	}

	public CartRequestDTO(Long id,
			@NotNull @Size(min = 1, message = "Cart must contain atleast one product") Set<CartProductDTO> products,
			@NotNull Long userId) {
		super();
		this.id = id;
		this.products = products;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<CartProductDTO> getProducts() {
		return products;
	}

	public void setProducts(Set<CartProductDTO> products) {
		this.products = products;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
