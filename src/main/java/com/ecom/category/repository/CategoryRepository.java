package com.ecom.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecom.category.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, CrudRepository<Category, Long> {

}
