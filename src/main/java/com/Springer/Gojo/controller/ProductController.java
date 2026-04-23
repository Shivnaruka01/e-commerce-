package com.Springer.Gojo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Springer.Gojo.Dto.product.request.ProductRequest;
import com.Springer.Gojo.Dto.product.response.ProductResponse;
import com.Springer.Gojo.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/Gojo/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "Endpoints for catalog and inventory management")
public class ProductController {

	private final ProductService productService;

	@Operation(
			summary = "Create a new product", 
			description = "Admin only. Adds a new product to the catalog."
		)
		@ApiResponse(responseCode = "201", description = "Product created successfully")
		@ApiResponse(responseCode = "403", description = "Forbidden - Requires Admin role")
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
		return new ResponseEntity<>(productService.createProduct(request), HttpStatus.CREATED);
	}

	@Operation(summary = "Get all products", description = "Fetches a list of all available products in the store.")
	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<List<ProductResponse>> getAllProducts() {
		return ResponseEntity.ok(productService.getAllProducts());
	}

	@Operation(summary = "Get product by ID", description = "Fetches details of a specific product using its ID.")
	@ApiResponse(responseCode = "404", description = "Product not found")
	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ProductResponse> getProductById(
			@Parameter(description = "ID of the product to fetch")@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@Operation(summary = "Update product details", description = "Admin only. Updates price, stock, or description of an existing product.")
	@PutMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
			@Valid @RequestBody ProductRequest request) {
		return ResponseEntity.ok(productService.updateProduct(id, request));
	}

	@Operation(summary = "Delete product", description = "Admin only. Removes a product from the catalog.")
	@ApiResponse(responseCode = "204", description = "Product deleted successfully")
	@DeleteMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteProduct(
			@Parameter(description = "ID of the product to delete") @PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}
