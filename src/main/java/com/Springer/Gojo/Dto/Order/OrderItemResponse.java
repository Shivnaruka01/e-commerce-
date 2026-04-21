package com.Springer.Gojo.Dto.Order;

import java.math.BigDecimal;

public record OrderItemResponse(

		Long productId, String productName, BigDecimal price, int quantity, BigDecimal subtotal) {

}
