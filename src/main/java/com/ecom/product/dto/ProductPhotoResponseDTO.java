package com.ecom.product.dto;

public class ProductPhotoResponseDTO {

	private Long id;

	private String title;

	private String imageUrl;

	private String thumbnailUrl;
	
	private boolean isCoverImage;

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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public boolean isCoverImage() {
		return isCoverImage;
	}

	public void setCoverImage(boolean isCoverImage) {
		this.isCoverImage = isCoverImage;
	}

	
	
}
