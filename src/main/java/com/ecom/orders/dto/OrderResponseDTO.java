package com.ecom.orders.dto;

import java.time.Instant;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ecom.product.dto.ProductResponseDTO;

public class OrderResponseDTO {

	@NotNull
	private Long id;

	@Size(max = 50)
	private String orderNum;

	@NotNull
	private Instant orderDate;

	@Size(max = 50)
	private String orderStatus;

	@NotNull
	private Set<ProductResponseDTO> products;

	@NotNull
	private Long userId;

	public OrderResponseDTO(@NotNull Long id, @Size(max = 50) String orderNum, @NotNull Instant orderDate,
			@Size(max = 50) String orderStatus, @NotNull Set<ProductResponseDTO> products, @NotNull Long userId) {
		super();
		this.id = id;
		this.orderNum = orderNum;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.products = products;
		this.userId = userId;
	}

	public OrderResponseDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public Instant getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Instant orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Set<ProductResponseDTO> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductResponseDTO> products) {
		this.products = products;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
