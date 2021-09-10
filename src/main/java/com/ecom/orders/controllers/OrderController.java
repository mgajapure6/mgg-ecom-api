package com.ecom.orders.controllers;

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
import com.ecom.orders.dto.OrderRequestDTO;
import com.ecom.orders.dto.OrderResponseDTO;
import com.ecom.orders.service.OrderService;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<PagedResponse<OrderResponseDTO>> getAllOrder(
			@RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponse<OrderResponseDTO> response = orderService.getAllOrder(page, size);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<OrderResponseDTO> addOrder(@Valid @RequestBody OrderRequestDTO orderRequest,
			@CurrentUser UserPrincipal currentUser) {
		OrderResponseDTO orderResponse = orderService.addOrder(orderRequest, currentUser);
		return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable(name = "id") Long id) {
		OrderResponseDTO order = orderService.getOrder(id);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable(name = "id") Long id,
			@Valid @RequestBody OrderRequestDTO updateOrderRequest, @CurrentUser UserPrincipal currentUser) {
		OrderResponseDTO order = orderService.updateOrder(id, updateOrderRequest, currentUser);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteOrder(@PathVariable(name = "id") Long id,
			@CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = orderService.deleteOrder(id, currentUser);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

}
