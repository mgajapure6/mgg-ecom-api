package com.ecom.category.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

public class CategoryRequestDTO {

	private Long id;

	@NotBlank
	private String name;
	
	private Long parentCategoryId;
	
	private MultipartFile categoryImage;

	public CategoryRequestDTO(Long id, @NotBlank String name, Long parentCategoryId) {
		super();
		this.id = id;
		this.name = name;
		this.parentCategoryId = parentCategoryId;
	}

	public CategoryRequestDTO() {
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

	public Long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public MultipartFile getCategoryImage() {
		return categoryImage;
	}

	public void setCategoryImage(MultipartFile categoryImage) {
		this.categoryImage = categoryImage;
	}

}
