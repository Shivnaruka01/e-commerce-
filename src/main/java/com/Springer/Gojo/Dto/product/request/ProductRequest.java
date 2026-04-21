package com.Springer.Gojo.Dto.product.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequest(
		@NotBlank(message = "Product name is required") @Size(max = 100, message = "name cannot exceed 100 characters") String name,

		@Size(max = 500, message = "Description cannot exceed 500 characters") @NotBlank(message = "Product description is required") String description,

		@NotNull(message = "Product price required") @DecimalMin(value = "0.01", message = "Price must be greater than zero") BigDecimal price,

		@NotNull(message = "Stock quantity is required") @Min(value = 0, message = "Stock cannot be negative") Integer stock) {

}
