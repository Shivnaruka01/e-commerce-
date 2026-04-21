package com.Springer.Gojo.service;

import com.Springer.Gojo.Dto.Cart.CartResponse;

public interface CartService {

	CartResponse addToCart(Long productId, int quantity);

	CartResponse removeFromCart(Long productId);

	CartResponse updateQuantity(Long productId, int quantity);

	CartResponse getCart();

	void clearCart();

}
