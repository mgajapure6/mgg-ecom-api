package com.ecom.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecom.product.model.Product;
import com.ecom.product.model.ProductPhoto;

@Repository
public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Long>, CrudRepository<ProductPhoto, Long> {

	List<ProductPhoto> findByProduct(Product product);

}
