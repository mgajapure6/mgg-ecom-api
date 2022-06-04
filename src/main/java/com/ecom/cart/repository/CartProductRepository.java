package com.ecom.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecom.cart.model.Cart;
import com.ecom.cart.model.CartProduct;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long>, CrudRepository<CartProduct, Long> {
	List<CartProduct> findByCart(Cart cart);
}
