package com.ecom.orders.dto;

import java.time.Instant;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ecom.product.dto.ProductDTO;
import com.sun.istack.internal.NotNull;

public class OrderDTO {

	private Long id;

	@Size(max = 50)
	private String orderNum;

	@NotBlank
	private Instant orderDate;

	@Size(max = 50)
	private String orderStatus;

	@NotNull
	private Set<ProductDTO> products;

	@NotNull
	private Long userId;

	public OrderDTO() {
		super();
	}

	public OrderDTO(Long id, @NotBlank @Size(max = 50) String orderNum, @NotBlank Instant orderDate,
			@NotBlank @Size(max = 50) String orderStatus, Set<ProductDTO> products, Long userId) {
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
