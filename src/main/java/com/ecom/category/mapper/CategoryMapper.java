package com.ecom.category.mapper;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.ecom.category.dto.CategoryRequestDTO;
import com.ecom.category.dto.CategoryResponseDTO;
import com.ecom.category.model.Category;

@Service
public class CategoryMapper {

	public static Category mapToCategory(@Valid CategoryRequestDTO categoryRequest) {
		Category category = new Category();
		category.setId(categoryRequest.getId());
		category.setName(categoryRequest.getName());
		category.setParentCategoryId(categoryRequest.getParentCategoryId());
		if(categoryRequest.getCategoryImage() != null){
			category.setImageName(categoryRequest.getCategoryImage().getOriginalFilename());
		}
		return category;
	}

	public static CategoryRequestDTO mapToCategoryRequestDTO(Category category) {
		return new CategoryRequestDTO(category.getId(), category.getName(), category.getParentCategoryId());
	}
	
	public static CategoryResponseDTO mapToCategoryResponseDTO(Category category, String imageUrl) {
		CategoryResponseDTO response = new CategoryResponseDTO();
		response.setId(category.getId());
		response.setName(category.getName());
		response.setParentCategoryId(category.getParentCategoryId());
		response.setImageUrl(imageUrl);
		return response;
	}

}
