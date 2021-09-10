package com.ecom.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecom.category.model.Category;
import com.ecom.product.model.Product;
import com.ecom.user.model.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, CrudRepository<Product, Long> {

	Page<Product> findByCategory(Category category, Pageable pageable);

	Page<Product> findByCategoryAndUser(Category category, User user, Pageable pageable);

	Page<Product> findByCreatedBy(Long id, Pageable pageable);

}
