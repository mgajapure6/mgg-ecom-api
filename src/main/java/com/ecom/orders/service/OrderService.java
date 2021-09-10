package com.ecom.orders.service;

import java.util.Collections;
import java.util.List;
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
import com.ecom.orders.dto.OrderDTO;
import com.ecom.orders.mapper.OrderMapper;
import com.ecom.orders.model.Order;
import com.ecom.orders.repository.OrderRepository;
import com.ecom.user.model.RoleName;
import com.ecom.user.model.User;
import com.ecom.user.repository.UserRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	public PagedResponse<OrderDTO> getAllOrder(Integer page, Integer size) {
		AppUtil.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstant.CREATED_AT);
		Page<Order> orders = orderRepository.findAll(pageable);
		List<OrderDTO> content = orders.getNumberOfElements() == 0 ? Collections.emptyList()
				: orders.getContent().stream().map(o -> OrderMapper.mapToOrderDTO(o)).collect(Collectors.toList());
		return new PagedResponse<>(content, orders.getNumber(), orders.getSize(), orders.getTotalElements(),
				orders.getTotalPages(), orders.isLast());
	}

	public OrderDTO addOrder(@Valid OrderDTO orderRequest, UserPrincipal currentUser) {
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER, AppConstant.ID, 1L));
		if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString()))) {
			Order order = OrderMapper.mapToOrder(orderRequest);
			order.setUser(user);
			Order newOrder = orderRepository.save(order);
			OrderDTO orderResponse = OrderMapper.mapToOrderDTO(newOrder);
			return orderResponse;
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

	public OrderDTO getOrder(Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ORDER, AppConstant.ID, id));
		OrderDTO orderDTO = OrderMapper.mapToOrderDTO(order);
		return orderDTO;
	}

	public OrderDTO updateOrder(Long id, @Valid OrderDTO newOrderRequest, UserPrincipal currentUser) {
		Order order = orderRepository.findById(newOrderRequest.getId()).orElseThrow(
				() -> new ResourceNotFoundException(AppConstant.ORDER, AppConstant.ID, newOrderRequest.getId()));

		order.removeAllProduct(order.getProducts());
		order = OrderMapper.mapToOrder(newOrderRequest);

		order = orderRepository.save(order);
		OrderDTO orderDTO = OrderMapper.mapToOrderDTO(order);
		return orderDTO;
	}

	public ApiResponse deleteOrder(Long id, UserPrincipal currentUser) {
		if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_VENDOR.toString()))) {
			orderRepository.deleteById(id);
			return new ApiResponse(Boolean.TRUE, AppMessages.CATEGORY_DELETE_SUCCESS);
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, AppMessages.ACCESS_DENIED);
		throw new UnauthorizedException(apiResponse);
	}

}
