package com.ecom.cart.dto;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.ecom.product.dto.ProductDTO;

public class CartResponseDTO {

	@NotNull
	private Long id;

	@NotNull
	private Set<ProductDTO> products;

	@NotNull
	private Long userId;

	public CartResponseDTO(@NotNull Long id, @NotNull Set<ProductDTO> products, @NotNull Long userId) {
		super();
		this.id = id;
		this.products = products;
		this.userId = userId;
	}

	public CartResponseDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<ProductDTO> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductDTO> products) {
		this.products = products;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
