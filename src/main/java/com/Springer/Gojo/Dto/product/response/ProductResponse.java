package com.Springer.Gojo.Dto.product.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductResponse(
		@Schema(example = "1")Long id, 
		@Schema(example = "Gojo Satoru")String name, 
		@Schema(example = "Noise-cancelling over-ear headphones")String description, 
		@Schema(example = "2400.00")BigDecimal price, 
		@Schema(example = "2")int stock) {

}
