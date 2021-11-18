package com.ecom.product.dto;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public class ProductDTO {

	private Long id;

	@NotBlank
	@Size(max = 40)
	private String prodName;

	@NotBlank
	@Size(max = 1000)
	private String prodDesc;

	@Size(max = 40)
	private String prodSku;

	@Size(max = 40)
	private String prodModel;

	@Size(max = 500)
	private String prodTags;

	@NotNull
	private Double prodPrice;

	@NotNull
	private Double prodSpecialPrice;

	@NotNull
	private Double prodQuantity;

	@NotNull
	private Set<ProductAttributeDTO> prodAttributes;

	@NotNull
	private Set<ProductPhotoDTO> prodPhotos;
	
	@NotNull
	private List<MultipartFile> imageFiles;

	@NotNull
	private Long categoryId;

	@NotNull
	private Long userId;

	public ProductDTO(Long id, @NotBlank @Size(max = 40) String prodName, @NotBlank @Size(max = 1000) String prodDesc,
			@Size(max = 40) String prodSku, @Size(max = 40) String prodModel, @Size(max = 500) String prodTags,
			@NotNull Double prodPrice, @NotNull Double prodSpecialPrice, @NotNull Double prodQuantity,
			@NotNull Set<ProductAttributeDTO> prodAttributes, @NotNull Set<ProductPhotoDTO> prodPhotos,
			@NotNull Long categoryId, @NotNull Long userId) {
		super();
		this.id = id;
		this.prodName = prodName;
		this.prodDesc = prodDesc;
		this.prodSku = prodSku;
		this.prodModel = prodModel;
		this.prodTags = prodTags;
		this.prodPrice = prodPrice;
		this.prodSpecialPrice = prodSpecialPrice;
		this.prodQuantity = prodQuantity;
		this.prodAttributes = prodAttributes;
		this.prodPhotos = prodPhotos;
		this.categoryId = categoryId;
		this.userId = userId;
	}

	public ProductDTO(Long id, @NotBlank @Size(max = 40) String prodName, @NotBlank @Size(max = 1000) String prodDesc,
			@Size(max = 40) String prodSku, @Size(max = 40) String prodModel, @Size(max = 500) String prodTags,
			@NotNull Double prodPrice, @NotNull Double prodSpecialPrice, @NotNull Double prodQuantity) {
		super();
		this.id = id;
		this.prodName = prodName;
		this.prodDesc = prodDesc;
		this.prodSku = prodSku;
		this.prodModel = prodModel;
		this.prodTags = prodTags;
		this.prodPrice = prodPrice;
		this.prodSpecialPrice = prodSpecialPrice;
		this.prodQuantity = prodQuantity;
	}

	public ProductDTO() {
		super();
	}

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

	public String getProdTags() {
		return prodTags;
	}

	public void setProdTags(String prodTags) {
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

	public Set<ProductAttributeDTO> getProdAttributes() {
		return prodAttributes;
	}

	public void setProdAttributes(Set<ProductAttributeDTO> prodAttributes) {
		this.prodAttributes = prodAttributes;
	}

	public Set<ProductPhotoDTO> getProdPhotos() {
		return prodPhotos;
	}

	public void setProdPhotos(Set<ProductPhotoDTO> prodPhotos) {
		this.prodPhotos = prodPhotos;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<MultipartFile> getImageFiles() {
		return imageFiles;
	}

	public void setImageFiles(List<MultipartFile> imageFiles) {
		this.imageFiles = imageFiles;
	}

	@Override
	public String toString() {
		return "ProductDTO [id=" + id + ", prodName=" + prodName + ", prodDesc=" + prodDesc + ", prodSku=" + prodSku
				+ ", prodModel=" + prodModel + ", prodTags=" + prodTags + ", prodPrice=" + prodPrice
				+ ", prodSpecialPrice=" + prodSpecialPrice + ", prodQuantity=" + prodQuantity + ", prodAttributes="
				+ prodAttributes + ", prodPhotos=" + prodPhotos + ", categoryId=" + categoryId + ", userId=" + userId
				+ "]";
	}

}
