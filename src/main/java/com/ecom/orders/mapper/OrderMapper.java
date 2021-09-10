package com.ecom.orders.mapper;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.ecom.orders.dto.OrderProductDTO;
import com.ecom.orders.dto.OrderRequestDTO;
import com.ecom.orders.dto.OrderResponseDTO;
import com.ecom.orders.model.Order;
import com.ecom.orders.model.OrderProduct;
import com.ecom.product.dto.ProductDTO;

@Service
public class OrderMapper {

	public static Order mapToOrder(@Valid OrderRequestDTO orderRequest) {
		Order order = new Order();
		order.setId(orderRequest.getId());
		order.setOrderDate(orderRequest.getOrderDate());
		order.setOrderNum(orderRequest.getOrderNum()==null ? OrderMapper.generateOrderNumber() : orderRequest.getOrderNum());
		order.setOrderStatus(orderRequest.getOrderStatus()==null ? OrderMapper.generateOrderStatus("PLA") : orderRequest.getOrderStatus());
		order.setProducts(orderRequest.getProducts().stream().map(p->mapToOrderProduct(p)).collect(Collectors.toSet()));
		return order;
	}

	public static OrderResponseDTO mapToOrderResponseDTO(Order newOrder, Set<ProductDTO> products) {
		OrderResponseDTO orderDTO = new OrderResponseDTO();
		orderDTO.setId(newOrder.getId());
		orderDTO.setOrderDate(newOrder.getOrderDate());
		orderDTO.setOrderNum(newOrder.getOrderNum());
		orderDTO.setOrderStatus(newOrder.getOrderStatus());
		orderDTO.setProducts(products);
		return orderDTO;
	}
	
	public static OrderProduct mapToOrderProduct(OrderProductDTO orderProductDTO) {
		return new OrderProduct(orderProductDTO.getId(), orderProductDTO.getProductId(), orderProductDTO.getProductQuantity().doubleValue());
		
	}
	
	
	public static String generateOrderNumber() {
		return UUID.randomUUID().toString();
	}
	
	public static String generateOrderStatus(String flag) {
		// AWAITING FULFILLMENT, AWAITING PAYMENT, AWAITING SHIPIMENT, PENDING, ON HOLD, PLACED, PROCESSING, WAITING FOR PAYMENT, DISPATCH, SHIPPING, DELIVERED, CANCELLED, DECLINED, REJECTED, REFUNDED, DISPUTED, 
		return flag.equals("AF") ? "AWAITING FULFILLMENT" : flag.equals("AP") ? "AWAITING PAYMENT" : flag.equals("AS") ? "AWAITING SHIPIMENT" : flag.equals("PLA") ? "ORDER PLACED" : "CANCELLED";
	}
	
	

}
