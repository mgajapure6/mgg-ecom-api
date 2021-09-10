package com.ecom.product.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ecom.category.model.Category;
import com.ecom.user.model.User;
import com.ecom.user.model.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product")
public class Product extends UserDateAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank
	@Column(name = "prod_name")
	@Size(max = 40)
	private String prodName;

	@NotBlank
	@Column(name = "prod_desc")
	@Size(max = 1000)
	private String prodDesc;

	@NotBlank
	@Column(name = "prod_sku")
	@Size(max = 40)
	private String prodSku;

	@NotBlank
	@Column(name = "prod_model")
	@Size(max = 40)
	private String prodModel;

	@NotBlank
	@Column(name = "prod_tags")
	@Size(max = 500)
	private String prodTags;

	@NotNull
	@Column(name = "prod_price")
	private Double prodPrice;

	@NotNull
	@Column(name = "prod_spl_price")
	private Double prodSpecialPrice;

	@NotNull
	@Column(name = "prod_quantity")
	private Double prodQuantity;

	@OneToMany(mappedBy = "product")
	private Set<ProductAttribute> prodAttributes = new HashSet<>();

	@OneToMany(mappedBy = "product")
	private Set<ProductPhoto> prodPhotos = new HashSet<>();

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public Product(Long id, @NotBlank @Size(max = 40) String prodName, @NotBlank @Size(max = 1000) String prodDesc,
			@NotBlank @Size(max = 40) String prodSku, @NotBlank @Size(max = 40) String prodModel,
			@NotBlank @Size(max = 500) String prodTags, @NotNull Double prodPrice, @NotNull Double prodSpecialPrice,
			@NotNull Double prodQuantity, Category category, Set<ProductAttribute> prodAttributes,
			Set<ProductPhoto> prodPhotos, User user) {
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
		this.category = category;
		this.prodAttributes = prodAttributes;
		this.prodPhotos = prodPhotos;
		this.user = user;
	}

	public Product(Long createdBy, Long updatedBy, Long id, @NotBlank @Size(max = 40) String prodName,
			@NotBlank @Size(max = 1000) String prodDesc, @NotBlank @Size(max = 40) String prodSku,
			@NotBlank @Size(max = 40) String prodModel, @NotBlank @Size(max = 500) String prodTags,
			@NotNull Double prodPrice, @NotNull Double prodSpecialPrice, @NotNull Double prodQuantity) {
		super(createdBy, updatedBy);
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

	public Product() {
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<ProductAttribute> getProdAttributes() {
		return prodAttributes;
	}

	public void setProdAttributes(Set<ProductAttribute> prodAttributes) {
		this.prodAttributes = prodAttributes;
	}

	public Product addProdAttributes(ProductAttribute prodAttributes) {
		this.prodAttributes.add(prodAttributes);
		prodAttributes.setProduct(this);
		return this;
	}

	public Product removeProdAttributes(ProductAttribute prodAttributes) {
		this.prodAttributes.remove(prodAttributes);
		prodAttributes.setProduct(null);
		return this;
	}

	public Product removeAllProdAttributes(Set<ProductAttribute> prodAttributes) {
		this.prodAttributes.removeAll(prodAttributes);
		prodAttributes.stream().forEach(pa -> {
			pa.setProduct(null);
		});
		return this;
	}

	public Product prodAttributes(Set<ProductAttribute> prodAttributes) {
		this.prodAttributes = prodAttributes;
		return this;
	}

	public Set<ProductPhoto> getProdPhotos() {
		return prodPhotos;
	}

	public void setProdPhotos(Set<ProductPhoto> prodPhotos) {
		this.prodPhotos = prodPhotos;
	}

	public Product addProdPhotos(ProductPhoto prodPhotos) {
		this.prodPhotos.add(prodPhotos);
		prodPhotos.setProduct(this);
		return this;
	}

	public Product removeProdPhotos(ProductPhoto prodPhotos) {
		this.prodPhotos.remove(prodPhotos);
		prodPhotos.setProduct(null);
		return this;
	}

	public Product removeAllProdPhotos(Set<ProductPhoto> prodPhotos) {
		this.prodPhotos.removeAll(prodPhotos);
		prodPhotos.stream().forEach(pp -> {
			pp.setProduct(null);
		});
		return this;
	}

	public Product prodPhotos(Set<ProductPhoto> prodPhotos) {
		this.prodPhotos = prodPhotos;
		return this;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", prodName=" + prodName + ", prodDesc=" + prodDesc + ", prodSku=" + prodSku
				+ ", prodModel=" + prodModel + ", prodTags=" + prodTags + ", prodPrice=" + prodPrice
				+ ", prodSpecialPrice=" + prodSpecialPrice + ", prodQuantity=" + prodQuantity + "]";
	}

}
