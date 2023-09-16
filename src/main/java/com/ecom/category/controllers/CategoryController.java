package com.ecom.category.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.ecom.category.dto.CategoryRequestDTO;
import com.ecom.category.dto.CategoryResponseDTO;
import com.ecom.category.service.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Category API", description = "")
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping
	// @PreAuthorize("hasRole('USER') or hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<PagedResponse<CategoryResponseDTO>> getAllCategory(
			@RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponse<CategoryResponseDTO> response = categoryService.getAllCategory(page, size);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryResponseDTO> addCategory(@Valid @ModelAttribute CategoryRequestDTO categoryRequest,
			@CurrentUser UserPrincipal currentUser) {
		CategoryResponseDTO categoryResponse = categoryService.addCategory(categoryRequest, currentUser);
		return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable(name = "id") Long id) {
		CategoryResponseDTO categoryResponse = categoryService.getCategory(id);
		return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable(name = "id") Long id,
			@Valid @RequestBody CategoryRequestDTO newCategoryRequest, @CurrentUser UserPrincipal currentUser) {
		CategoryResponseDTO categoryResponse = categoryService.updateCategory(id, newCategoryRequest, currentUser);
		return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable(name = "id") Long id,
			@CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = categoryService.deleteCategory(id, currentUser);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

}
