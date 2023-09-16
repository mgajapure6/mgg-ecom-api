package com.ecom.product.dto;

import javax.validation.constraints.NotBlank;

public class ProductAttributeRequestDTO {

	private Long id;

	@NotBlank
	private String name;

	@NotBlank
	private String value;

	public ProductAttributeRequestDTO(Long id, @NotBlank String name, @NotBlank String value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public ProductAttributeRequestDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ProductAttributeDTO [id=" + id + ", name=" + name + ", value=" + value + "]";
	}

}
