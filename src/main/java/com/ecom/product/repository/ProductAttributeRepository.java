package com.ecom.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecom.product.model.Product;
import com.ecom.product.model.ProductAttribute;

@Repository
public interface ProductAttributeRepository
		extends JpaRepository<ProductAttribute, Long>, CrudRepository<ProductAttribute, Long> {

	List<ProductAttribute> findByProduct(Product product);

}
