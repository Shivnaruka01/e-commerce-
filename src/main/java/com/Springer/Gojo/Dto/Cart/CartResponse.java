package com.Springer.Gojo.Dto.Cart;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record CartResponse(

		@Schema(example = "101")Long cartId, 
		List<CartItemResponse> items, 
		@Schema(example = "2400.00")BigDecimal totalPrice) {

}
