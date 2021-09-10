package com.ecom.orders.dto;

import java.time.Instant;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OrderRequestDTO {
	private Long id;

	@Size(max = 50)
	private String orderNum;

	@NotNull
	private Instant orderDate;

	@Size(max = 50)
	private String orderStatus;

	@NotNull
	@Size(min = 1, message = "Order must contain atleast one product")
	private Set<OrderProductDTO> products;

	@NotNull
	private Long userId;

	public OrderRequestDTO() {
		super();
	}

	public OrderRequestDTO(Long id, String orderNum, Instant orderDate, String orderStatus, Set<OrderProductDTO> products,
			Long userId) {
		super();
		this.id = id;
		this.orderNum = orderNum;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.products = products;
		this.userId = userId;
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

	public Set<OrderProductDTO> getProducts() {
		return products;
	}

	public void setProducts(Set<OrderProductDTO> products) {
		this.products = products;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
