package com.ecom.cart.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecom.cart.model.Cart;
import com.ecom.user.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, CrudRepository<Cart, Long> {

	Page<Cart> findByUser(Long userId, Pageable pageable);

	Optional<Cart> findByUser(User user);

}
