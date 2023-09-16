package com.ecom.product.dto;

import java.util.List;
import java.util.Set;

public class ProductResponseDTO {

	private Long id;

	private String prodName;

	private String prodDesc;

	private String prodSku;

	private String prodModel;

	private List<String> prodTags;

	private Double prodPrice;

	private Double prodSpecialPrice;

	private Double prodQuantity;

	private Set<ProductAttributeResponseDTO> prodAttributes;

	private Set<ProductPhotoResponseDTO> images;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdDesc() {
		return prodDesc;
	}

	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}

	public String getProdSku() {
		return prodSku;
	}

	public void setProdSku(String prodSku) {
		this.prodSku = prodSku;
	}

	public String getProdModel() {
		return prodModel;
	}

	public void setProdModel(String prodModel) {
		this.prodModel = prodModel;
	}

	public List<String> getProdTags() {
		return prodTags;
	}

	public void setProdTags(List<String> prodTags) {
		this.prodTags = prodTags;
	}

	public Double getProdPrice() {
		return prodPrice;
	}

	public void setProdPrice(Double prodPrice) {
		this.prodPrice = prodPrice;
	}

	public Double getProdSpecialPrice() {
		return prodSpecialPrice;
	}

	public void setProdSpecialPrice(Double prodSpecialPrice) {
		this.prodSpecialPrice = prodSpecialPrice;
	}

	public Double getProdQuantity() {
		return prodQuantity;
	}

	public void setProdQuantity(Double prodQuantity) {
		this.prodQuantity = prodQuantity;
	}

	public Set<ProductAttributeResponseDTO> getProdAttributes() {
		return prodAttributes;
	}

	public void setProdAttributes(Set<ProductAttributeResponseDTO> prodAttributes) {
		this.prodAttributes = prodAttributes;
	}

	public Set<ProductPhotoResponseDTO> getImages() {
		return images;
	}

	public void setImages(Set<ProductPhotoResponseDTO> images) {
		this.images = images;
	}

	
	
	
	
}
