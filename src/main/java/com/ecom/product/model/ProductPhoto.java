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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

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

	@Column(name = "image_name")
	private String imageName;
	
	@Column(name = "thumbnail_image_name")
	private String thumbnailImageName;
	
	@Column(name = "cover_image")
	private Boolean coverImage;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	
	@Transient
	@JsonIgnore
	private MultipartFile file;
	

	public ProductPhoto(Long id, String title, String imageName, String thumbnailImageName, Boolean coverImage, Product product) {
		super();
		this.id = id;
		this.title = title;
		this.imageName = imageName;
		this.thumbnailImageName = thumbnailImageName;
		this.product = product;
		this.coverImage = coverImage;
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

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getThumbnailImageName() {
		return thumbnailImageName;
	}

	public void setThumbnailImageName(String thumbnailImageName) {
		this.thumbnailImageName = thumbnailImageName;
	}

	public Boolean getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(Boolean coverImage) {
		this.coverImage = coverImage;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	

}
