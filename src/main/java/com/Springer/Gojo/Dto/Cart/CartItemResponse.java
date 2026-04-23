package com.Springer.Gojo.Dto.Cart;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public record CartItemResponse(

		@Schema(example = "1") Long productId, 
		@Schema(example = "Gojo Satoru Figure")String productName, 
		@Schema(example = "1200.00")BigDecimal price, 
		@Schema(example = "2")int quantity, 
		@Schema(example = "2400.00")BigDecimal subtotal) {}
