package com.ecom.category.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.app.payload.ApiResponse;
import com.ecom.app.payload.PagedResponse;
import com.ecom.app.security.CurrentUser;
import com.ecom.app.security.UserPrincipal;
import com.ecom.app.utils.AppConstant;
import com.ecom.category.dto.CategoryDTO;
import com.ecom.category.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping
	// @PreAuthorize("hasRole('USER') or hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<PagedResponse<CategoryDTO>> getAllCategory(
			@RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponse<CategoryDTO> response = categoryService.getAllCategory(page, size);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryRequest,
			@CurrentUser UserPrincipal currentUser) {
		CategoryDTO categoryResponse = categoryService.addCategory(categoryRequest, currentUser);
		return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryDTO> getCategory(@PathVariable(name = "id") Long id) {
		CategoryDTO category = categoryService.getCategory(id);
		return new ResponseEntity<>(category, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable(name = "id") Long id,
			@Valid @RequestBody CategoryDTO newCategoryRequest, @CurrentUser UserPrincipal currentUser) {
		CategoryDTO category = categoryService.updateCategory(id, newCategoryRequest, currentUser);
		return new ResponseEntity<>(category, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable(name = "id") Long id,
			@CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = categoryService.deleteCategory(id, currentUser);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

}
