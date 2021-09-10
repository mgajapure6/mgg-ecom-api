package com.ecom.orders.mapper;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.ecom.orders.dto.OrderDTO;
import com.ecom.orders.model.Order;
import com.ecom.product.mapper.ProductMapper;

@Service
public class OrderMapper {

	public static Order mapToOrder(@Valid OrderDTO orderRequest) {
		Order order = new Order();
		order.setId(orderRequest.getId());
		order.setOrderDate(orderRequest.getOrderDate());
		order.setOrderNum(orderRequest.getOrderNum());
		order.setOrderStatus(orderRequest.getOrderStatus());
		order.setProducts(orderRequest.getProducts().stream().map(opdto -> ProductMapper.mapToProduct(opdto))
				.collect(Collectors.toSet()));
		return order;
	}

	public static OrderDTO mapToOrderDTO(Order newOrder) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setId(newOrder.getId());
		orderDTO.setOrderDate(newOrder.getOrderDate());
		orderDTO.setOrderNum(newOrder.getOrderNum());
		orderDTO.setOrderStatus(newOrder.getOrderStatus());
		orderDTO.setProducts(newOrder.getProducts().stream().map(op -> ProductMapper.mapToProductDTO(op))
				.collect(Collectors.toSet()));
		return orderDTO;
	}

}
