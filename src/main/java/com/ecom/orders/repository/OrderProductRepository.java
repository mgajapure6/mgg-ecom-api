package com.ecom.orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecom.orders.model.Order;
import com.ecom.orders.model.OrderProduct;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>, CrudRepository<OrderProduct, Long>  {
	List<OrderProduct> findByOrder(Order order);
}
