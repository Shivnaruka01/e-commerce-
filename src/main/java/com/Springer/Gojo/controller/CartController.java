package com.Springer.Gojo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Springer.Gojo.Dto.Cart.CartResponse;
import com.Springer.Gojo.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart Controller", description = "Manage items in the user's shopping cart")
public class CartController {

	private final CartService cartService;
	
	@Operation(summary = "Add item to cart", description = "Adds a product to the user's cart. If the item exists, it increases the quantity.")
    @ApiResponse(responseCode = "200", description = "Item added successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
	@PostMapping("/items")
	public ResponseEntity<CartResponse> addToCart(
		    @Parameter(description = "ID of the product to add", example = "1") @Min(1) @RequestParam Long productId, 
		    @Parameter(description = "Quantity of the product", example = "2") @RequestParam int quantity) {
		return ResponseEntity.ok(cartService.addToCart(productId, quantity));
	}

    @Operation(summary = "Remove item from cart", description = "Completely removes a specific product from the cart")
	@DeleteMapping("/items/{productId}")
	public ResponseEntity<CartResponse> removeFromCart(
			@Parameter(description = "ID of the product to remove") @PathVariable Long productId) {
		return ResponseEntity.ok(cartService.removeFromCart(productId));
	}

    @Operation(summary = "Update item quantity", description = "Sets the quantity of an existing cart item to a specific value")
	@PutMapping("/items")
	public ResponseEntity<CartResponse> updateQuantity(
			@Parameter(description = "ID of the product") @Min(1) @RequestParam Long productId,
			@Parameter(description = "New quantity value", example = "5") @RequestParam int quantity) {
		return ResponseEntity.ok(cartService.updateQuantity(productId, quantity));
	}

    @Operation(summary = "Get current cart", description = "Fetches all items and total price for the authenticated user's cart")
	@GetMapping
	public ResponseEntity<CartResponse> getCart() {
		return ResponseEntity.ok(cartService.getCart());
	}

    @Operation(summary = "Clear entire cart", description = "Removes all items from the user's cart")
    @ApiResponse(responseCode = "204", description = "Cart cleared successfully")
	@DeleteMapping("/clear")
	public ResponseEntity<Void> clearCart() {
		cartService.clearCart();
		return ResponseEntity.noContent().build();
	}

}
