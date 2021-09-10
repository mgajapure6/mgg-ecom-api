package com.ecom.product.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.ecom.user.model.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product_photo")
public class ProductPhoto extends DateAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "title")
	private String title;

	@NotBlank
	@Column(name = "url")
	private String url;

	@NotBlank
	@Column(name = "thumbnail_url")
	private String thumbnailUrl;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	public ProductPhoto(Long id, String title, String url, String thumbnailUrl, Product product) {
		super();
		this.id = id;
		this.title = title;
		this.url = url;
		this.thumbnailUrl = thumbnailUrl;
		this.product = product;
	}

	public ProductPhoto() {
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "ProductPhoto [id=" + id + ", title=" + title + ", url=" + url + ", thumbnailUrl=" + thumbnailUrl
				+ ", product=" + product + "]";
	}

}
