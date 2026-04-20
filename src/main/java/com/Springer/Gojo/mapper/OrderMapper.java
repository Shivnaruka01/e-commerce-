package com.Springer.Gojo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.Springer.Gojo.Dto.Order.OrderItemResponse;
import com.Springer.Gojo.Dto.Order.OrderResponse;
import com.Springer.Gojo.entity.Order;
import com.Springer.Gojo.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Use this for general history/list views
	@Mapping(target = "orderId", source = "id")
	@Mapping(target = "paymentId", ignore = true) // No payment ID needed here
	OrderResponse toResponse(Order order);
	
	// Use this specifically in placeOrder()
	@Mapping(target = "orderId", source = "order.id")
	@Mapping(target = "paymentId", source = "paymentId") 
	OrderResponse toResponseWithPayment(Order order, Long paymentId);

	@Mapping(target = "productId", source = "product.id")
	OrderItemResponse toItemResponse(OrderItem item);
}
