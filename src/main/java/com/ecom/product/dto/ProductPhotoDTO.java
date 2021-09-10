package com.ecom.product.dto;

import javax.validation.constraints.NotBlank;

public class ProductPhotoDTO {

	private Long id;

	@NotBlank
	private String title;

	@NotBlank
	private String url;

	@NotBlank
	private String thumbnailUrl;

	public ProductPhotoDTO(Long id, String title, @NotBlank String url, @NotBlank String thumbnailUrl) {
		super();
		this.id = id;
		this.title = title;
		this.url = url;
		this.thumbnailUrl = thumbnailUrl;
	}

	public ProductPhotoDTO() {
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	@Override
	public String toString() {
		return "ProductPhotoDTO [id=" + id + ", title=" + title + ", url=" + url + ", thumbnailUrl=" + thumbnailUrl
				+ "]";
	}

}
