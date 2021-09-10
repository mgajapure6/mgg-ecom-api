package com.ecom.category.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.ecom.app.exceptions.ResourceNotFoundException;
import com.ecom.app.exceptions.UnauthorizedException;
import com.ecom.app.payload.ApiResponse;
import com.ecom.app.payload.PagedResponse;
import com.ecom.app.security.UserPrincipal;
import com.ecom.app.utils.AppConstant;
import com.ecom.app.utils.AppMessages;
import com.ecom.app.utils.AppUtil;
import com.ecom.category.dto.CategoryDTO;
import com.ecom.category.mapper.CategoryMapper;
import com.ecom.category.model.Category;
import com.ecom.category.repository.CategoryRepository;
import com.ecom.product.model.Product;
import com.ecom.user.model.RoleName;
import com.ecom.user.model.User;
import com.ecom.user.repository.UserRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UserRepository userRepository;

	public PagedResponse<CategoryDTO> getAllCategory(Integer page, Integer size) {
		AppUtil.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstant.CREATED_AT);
		Page<Category> categories = categoryRepository.findAll(pageable);
		List<CategoryDTO> content = categories.getNumberOfElements() == 0 ? Collections.emptyList()
				: categories.getContent().stream().map(c -> CategoryMapper.mapToCategoryDTO(c))
						.collect(Collectors.toList());
		return new PagedResponse<>(content, categories.getNumber(), categories.getSize(), categories.getTotalElements(),
				categories.getTotalPages(), categories.isLast());
	}

	public CategoryDTO addCategory(CategoryDTO categoryRequest, UserPrincipal currentUser) {
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER, AppConstant.ID, 1L));

		Category category = CategoryMapper.mapToCategory(categoryRequest);
		category.setUser(user);
		Category newCategory = categoryRepository.save(category);
		CategoryDTO categoryResponse = CategoryMapper.mapToCategoryDTO(newCategory);
		return categoryResponse;
	}

	public CategoryDTO getCategory(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY, AppConstant.ID, id));
		CategoryDTO categoryDTO = CategoryMapper.mapToCategoryDTO(category);
		return categoryDTO;
	}

	public CategoryDTO updateCategory(Long id, @Valid CategoryDTO newCategoryRequest, UserPrincipal currentUser) {
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER, AppConstant.ID, 1L));
		Category category = categoryRepository.findById(newCategoryRequest.getId()).orElseThrow(
				() -> new ResourceNotFoundException(AppConstant.CATEGORY, AppConstant.ID, newCategoryRequest.getId()));
		if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			Set<Product> products = category.getProducts();
			category = CategoryMapper.mapToCategory(newCategoryRequest);
			category.removeAllProducts(products);
			category.addAllProducts(products);
			category.setUser(user);
			category = categoryRepository.save(category);
			CategoryDTO categoryDTO = CategoryMapper.mapToCategoryDTO(category);
			return categoryDTO;
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

	public ApiResponse deleteCategory(Long categoryId, UserPrincipal currentUser) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY, AppConstant.ID, categoryId));
		if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			category.removeAllProducts(category.getProducts());
			categoryRepository.deleteById(categoryId);
			return new ApiResponse(Boolean.TRUE, AppMessages.CATEGORY_DELETE_SUCCESS);
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

}
