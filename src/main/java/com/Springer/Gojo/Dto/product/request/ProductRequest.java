package com.Springer.Gojo.Dto.product.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequest(
		@Schema(description = "Full name of the product", example = "Wireless Headphones")
		@NotBlank(message = "Product name is required")
		@Size(max = 100, message = "name cannot exceed 100 characters") 
		String name,

	    @Schema(description = "Detailed product information", example = "Noise-cancelling over-ear headphones")
		@Size(max = 500, message = "Description cannot exceed 500 characters") 
		@NotBlank(message = "Product description is required") 
		String description,

		@Schema(description = "Price per unit", example = "99.99")
		@NotNull(message = "Product price required")
		@DecimalMin(value = "0.01", message = "Price must be greater than zero") 
		BigDecimal price,

		@Schema(description = "Available quantity in inventory", example = "50")
		@NotNull(message = "Stock quantity is required") 
		@Min(value = 0, message = "Stock cannot be negative") 
		Integer stock) {}
