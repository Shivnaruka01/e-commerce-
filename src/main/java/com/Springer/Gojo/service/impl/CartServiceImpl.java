package com.Springer.Gojo.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Springer.Gojo.Dto.Cart.CartResponse;
import com.Springer.Gojo.entity.Cart;
import com.Springer.Gojo.entity.CartItem;
import com.Springer.Gojo.entity.Product;
import com.Springer.Gojo.entity.User;
import com.Springer.Gojo.exception.ResourceNotFoundException;
import com.Springer.Gojo.mapper.CartMapper;
import com.Springer.Gojo.repository.CartRepository;
import com.Springer.Gojo.repository.ProductRepository;
import com.Springer.Gojo.repository.UserRepository;
import com.Springer.Gojo.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final CartMapper cartMapper;

	@Override
	@Transactional
	public CartResponse addToCart(Long productId, int quantity) {
		User user = getCurrentUser();
		Cart cart = cartRepository.findByUser(user).orElseGet(() -> createCart(user));
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found."));

		cart.addItem(product, quantity);

		Cart savedCart = cartRepository.save(cart);

		return cartMapper.toResponse(savedCart);
	}

	@Override
	@Transactional
	public CartResponse removeFromCart(Long productId) {
		User user = getCurrentUser();
		Cart cart = getCartOrThrow(user);
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found."));

		cart.removeItem(product);
		return cartMapper.toResponse(cartRepository.save(cart));
	}

	@Override
	@Transactional
	public CartResponse updateQuantity(Long productId, int quantity) {
		User user = getCurrentUser();
		Cart cart = getCartOrThrow(user);

		CartItem cartItem = cart.getItems().stream().filter(i -> i.getProduct().getId().equals(productId)).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Item not found"));

		cartItem.updateQuantity(quantity);
		return cartMapper.toResponse(cartRepository.save(cart));
	}

	@Override
	@Transactional(readOnly = true)
	public CartResponse getCart() {
		User user = getCurrentUser();
		Cart cart = getCartOrThrow(user);

		return cartMapper.toResponse(cart);
	}

	@Override
	@Transactional
	public void clearCart() {
		User user = getCurrentUser();
		Cart cart = getCartOrThrow(user);
		cart.clear();
		cartRepository.save(cart);
	}

	private User getCurrentUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found."));
	}

	private Cart getCartOrThrow(User user) {
		return cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found."));
	}

	private Cart createCart(User user) {
		Cart cart = Cart.builder().user(user)
//					   .items(new ArrayList<>())// @Builder.Default handle this
				.build();
		return cartRepository.save(cart);
	}
}
