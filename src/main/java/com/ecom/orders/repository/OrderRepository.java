package com.ecom.orders.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecom.orders.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, CrudRepository<Order, Long> {

	Page<Order> findByUser(Long userId, Pageable pageable);

}
