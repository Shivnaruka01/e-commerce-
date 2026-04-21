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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
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
		
		log.atInfo()
			.setMessage("Item added to cart")
			.addKeyValue("productId", productId)
			.addKeyValue("quantity", quantity)
			.addKeyValue("cartTotal", savedCart.getTotalPrice())
			.log();

		return cartMapper.toResponse(savedCart);
	}

	@Override
	@Transactional
	public CartResponse removeFromCart(Long productId) {
		User user = getCurrentUser();
		Cart cart = getCartOrThrow(user);
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> {
			        log.atWarn()
			           .setMessage("Remove failed: Product not found")
			           .addKeyValue("productId", productId)
			           .log();
			        return new ResourceNotFoundException("Product not found.");
			    });

		cart.removeItem(product);
		
		Cart savedNewCart = cartRepository.save(cart);
		
		log.atInfo()
			.setMessage("Item removed from cart")
			.addKeyValue("productId", productId)
			.log();
		return cartMapper.toResponse(savedNewCart);
	}

	@Override
	@Transactional
	public CartResponse updateQuantity(Long productId, int quantity) {
		User user = getCurrentUser();
		Cart cart = getCartOrThrow(user);

		CartItem cartItem = cart.getItems().stream().filter(i -> i.getProduct().getId().equals(productId)).findFirst()
				.orElseThrow(() -> {
					log.atWarn()
						.setMessage("Attempted to update non-existent cart item")
						.addKeyValue("productId", productId)
						.log();
					return new ResourceNotFoundException("Item not found");	
				});

		cartItem.updateQuantity(quantity);
		
		Cart updatedCart = cartRepository.save(cart);
				
		log.atInfo()
			.setMessage("Item quantity updated in cart")
			.addKeyValue("productId", productId)
			.addKeyValue("quantity", quantity)
			.log();
		
		return cartMapper.toResponse(updatedCart);
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
		log.atInfo()
			.setMessage("Cart clear successfully")
			.log();	
	}

	private User getCurrentUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByEmail(email).orElseThrow(() ->{
			log.atError().setMessage("Security Context Error: Authenticated user not found in DB").log();
			return new ResourceNotFoundException("User not found.");
		});
	}

	private Cart getCartOrThrow(User user) {
		return cartRepository.findByUser(user).orElseThrow(() -> {
			// Logged as WARN because a user without a cart shouldn't be hitting 'update' or 'clear'
            log.atWarn()
               .setMessage("Cart lookup failed")
               .addKeyValue("user", user.getEmail())
               .log();
            return new ResourceNotFoundException("Cart not found.");
		});
	}

	private Cart createCart(User user) {
		Cart cart = Cart.builder().user(user)
//					   .items(new ArrayList<>())// @Builder.Default handle this
				.build();
		Cart savedcart = cartRepository.save(cart);
		
		// Logged as DEBUG: Vital for tracking new user conversion/onboarding
        log.atDebug()
           .setMessage("New cart initialized for user")
           .addKeyValue("user", user.getEmail())
           .log();
		
		return savedcart;
	}
}
