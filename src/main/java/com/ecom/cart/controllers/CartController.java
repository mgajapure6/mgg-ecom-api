package com.ecom.cart.controllers;

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
import com.ecom.cart.dto.CartRequestDTO;
import com.ecom.cart.dto.CartResponseDTO;
import com.ecom.cart.service.CartService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cart API", description = "")
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@GetMapping
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<PagedResponse<CartResponseDTO>> getAllCarts(
			@RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponse<CartResponseDTO> response = cartService.getAllCart(page, size);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/byUser")
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<CartResponseDTO> getCartByUser(@CurrentUser UserPrincipal currentUser) {
		CartResponseDTO cart = cartService.getCartByUser(currentUser);
		return new ResponseEntity<>(cart, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<CartResponseDTO> addCart(@Valid @RequestBody CartRequestDTO cartRequest,
			@CurrentUser UserPrincipal currentUser) {
		CartResponseDTO cartResponse = cartService.addCart(cartRequest, currentUser);
		return new ResponseEntity<>(cartResponse, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<CartResponseDTO> getCart(@PathVariable(name = "id") Long id) {
		CartResponseDTO cart = cartService.getCart(id);
		return new ResponseEntity<>(cart, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<CartResponseDTO> updateCart(@PathVariable(name = "id") Long id,
			@Valid @RequestBody CartRequestDTO updateCartRequest, @CurrentUser UserPrincipal currentUser) {
		CartResponseDTO cart = cartService.updateCart(id, updateCartRequest, currentUser);
		return new ResponseEntity<>(cart, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteCart(@PathVariable(name = "id") Long id,
			@CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = cartService.deleteCart(id, currentUser);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

}
