package com.ecom.orders.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class OrderProductDTO {

	@NotNull
	private Long id;

	@NotNull
	private Long productId;

	@NotNull
	@DecimalMin(value = "0.001", inclusive = false)
    @Digits(integer=2, fraction=3)
	private BigDecimal productQuantity;

	public OrderProductDTO(Long id, Long productId, BigDecimal productQuantity) {
		super();
		this.id = id;
		this.productId = productId;
		this.productQuantity = productQuantity;
	}

	public OrderProductDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public BigDecimal getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(BigDecimal productQuantity) {
		this.productQuantity = productQuantity;
	}

}
