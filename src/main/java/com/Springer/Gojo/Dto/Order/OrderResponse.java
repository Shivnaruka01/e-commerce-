package com.Springer.Gojo.Dto.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.Springer.Gojo.entity.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderResponse(

		@Schema(example = "501") Long orderId, 
		@Schema(example = "2026-04-20T10:15:30") LocalDateTime orderDate, 
		@Schema(example = "CONFIRMED") OrderStatus status, 
		@Schema(example = "2400.00") BigDecimal totalAmount,
		List<OrderItemResponse> items,
		@Schema(example = "901") Long paymentId
		){}
