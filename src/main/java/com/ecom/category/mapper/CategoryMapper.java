package com.ecom.category.mapper;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.ecom.category.dto.CategoryDTO;
import com.ecom.category.model.Category;

@Service
public class CategoryMapper {

	public static Category mapToCategory(@Valid CategoryDTO categoryRequest) {
		return new Category(categoryRequest.getId(), categoryRequest.getName());
	}

	public static CategoryDTO mapToCategoryDTO(Category category) {
		return new CategoryDTO(category.getId(), category.getName());
	}

}
