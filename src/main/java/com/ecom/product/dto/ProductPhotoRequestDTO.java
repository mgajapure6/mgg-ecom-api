package com.ecom.product.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

public class ProductPhotoRequestDTO {

	private Long id;

	@NotBlank
	private String title;

	@NotBlank
	private MultipartFile image;

	private Boolean coverImage;

	private Boolean thumbnail;

	public ProductPhotoRequestDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public Boolean getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(Boolean coverImage) {
		this.coverImage = coverImage;
	}

	public Boolean getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Boolean thumbnail) {
		this.thumbnail = thumbnail;
	}

}
