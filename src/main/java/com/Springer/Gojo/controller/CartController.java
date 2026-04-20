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

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@PostMapping("/items")
	public ResponseEntity<CartResponse> addToCart(@Min(1) @RequestParam Long productId, @RequestParam int quantity) {
		return ResponseEntity.ok(cartService.addToCart(productId, quantity));
	}

	@DeleteMapping("/items/{productId}")
	public ResponseEntity<CartResponse> removeFromCart(@PathVariable Long productId) {
		return ResponseEntity.ok(cartService.removeFromCart(productId));
	}

	@PutMapping("/items")
	public ResponseEntity<CartResponse> updateQuantity(@Min(1) @RequestParam Long productId,
			@RequestParam int quantity) {
		return ResponseEntity.ok(cartService.updateQuantity(productId, quantity));
	}

	@GetMapping
	public ResponseEntity<CartResponse> getCart() {
		return ResponseEntity.ok(cartService.getCart());
	}

	@DeleteMapping("/clear")
	public ResponseEntity<Void> clearCart() {
		cartService.clearCart();
		return ResponseEntity.noContent().build();
	}

}
