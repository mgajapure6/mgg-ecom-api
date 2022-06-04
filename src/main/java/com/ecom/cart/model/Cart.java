package com.ecom.cart.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ecom.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CartProduct> products = new HashSet<>();

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public Cart() {
		super();
	}

	public Cart(Long id, Set<CartProduct> products, User user) {
		super();
		this.id = id;
		this.products = products;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<CartProduct> getProducts() {
		return products;
	}

	public void setProducts(Set<CartProduct> products) {
		this.products = products;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Cart addCartProduct(CartProduct product) {
		this.products.add(product);
		return this;
	}

	public Cart removeCartProduct(CartProduct product) {
		this.products.remove(product);
		return this;
	}

	public Cart removeAllCartProducts(Set<CartProduct> products) {
		this.products.removeAll(products);
		return this;
	}

}
