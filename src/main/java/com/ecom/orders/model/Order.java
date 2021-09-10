package com.ecom.orders.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ecom.product.model.Product;
import com.ecom.user.model.DateAudit;
import com.ecom.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "app_order")
public class Order extends DateAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank
	@Column(name = "order_num")
	@Size(max = 50)
	private String orderNum;

	@NotBlank
	@Column(name = "order_date")
	private Instant orderDate;

	@NotBlank
	@Column(name = "order_status")
	@Size(max = 50)
	private String orderStatus;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "order_product", joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
	private Set<Product> products = new HashSet<>();

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public Order() {
		super();
	}

	public Order(Long id, @NotBlank @Size(max = 50) String orderNum, @NotBlank Instant orderDate,
			@NotBlank @Size(max = 50) String orderStatus, Set<Product> products, User user) {
		super();
		this.id = id;
		this.orderNum = orderNum;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.products = products;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public Instant getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Instant orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Order addProduct(Product product) {
		this.products.add(product);
		return this;
	}

	public Order removeProduct(Product product) {
		this.products.remove(product);
		return this;
	}

	public Order removeAllProduct(Set<Product> products) {
		this.products.removeAll(products);
		return this;
	}

}
