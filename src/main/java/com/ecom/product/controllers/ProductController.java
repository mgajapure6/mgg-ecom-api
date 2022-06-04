package com.ecom.product.controllers;

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
import com.ecom.product.dto.ProductDTO;
import com.ecom.product.model.Product;
import com.ecom.product.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ptoduct API", description = "")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	@PreAuthorize("hasRole('USER') or hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<PagedResponse<Product>> getAllProducts(
			@RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponse<Product> response = productService.getAllProducts(page, size);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/category/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<PagedResponse<ProductDTO>> getProductsByCategory(
			@RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size,
			@PathVariable(name = "id") Long categoryId) {
		PagedResponse<ProductDTO> response = productService.getProductsByCategory(categoryId, page, size);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/category/{categoryId}/user/{userId}")
	@PreAuthorize("hasRole('USER') or hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<PagedResponse<ProductDTO>> getProductsByCategoryAndUser(
			@RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size,
			@PathVariable(name = "categoryId") Long categoryId, @PathVariable(name = "userId") Long userId) {
		PagedResponse<ProductDTO> response = productService.getProductsByCategoryAndUser(categoryId, userId, page,
				size);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productRequest,
			@CurrentUser UserPrincipal currentUser) {
		ProductDTO productResponse = productService.addProduct(productRequest, currentUser);
		return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	// @PreAuthorize("hasRole('USER')")
	public ResponseEntity<ProductDTO> getProduct(@PathVariable(name = "id") Long id) {
		ProductDTO productResponse = productService.getProduct(id);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable(name = "id") Long productId,
			@Valid @RequestBody ProductDTO newProductRequest, @CurrentUser UserPrincipal currentUser) {
		ProductDTO productResponse = productService.updateProduct(productId, newProductRequest, currentUser);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable(name = "id") Long id,
			@CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = productService.deleteProduct(id, currentUser);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

}
