package com.Springer.Gojo.Dto.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.Springer.Gojo.entity.OrderStatus;

public record OrderResponse(

		Long orderId, 
		LocalDateTime orderDate, 
		OrderStatus status, 
		BigDecimal totalAmount,
		List<OrderItemResponse> items,
		Long paymentId
		){}
