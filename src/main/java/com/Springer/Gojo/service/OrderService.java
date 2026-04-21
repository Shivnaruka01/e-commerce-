package com.Springer.Gojo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.Springer.Gojo.Dto.Order.OrderResponse;
import com.Springer.Gojo.entity.OrderStatus;

public interface OrderService {

	OrderResponse placeOrder();

	// Not needed because we are now going to use PAGINATION
	// List<OrderResponse> getOrderHistory();
	Page<OrderResponse> getOrderHistory(Pageable pageable);

	Page<OrderResponse> getAllOrders(Pageable pageable);

	Page<OrderResponse> getOrdersByUserForAdmin(Long userId, Pageable pageable);

	OrderResponse getOrderById(Long id);

	OrderResponse updateOrderStatus(Long id, OrderStatus status);
	
	OrderResponse cancelOrder(Long id);
}
