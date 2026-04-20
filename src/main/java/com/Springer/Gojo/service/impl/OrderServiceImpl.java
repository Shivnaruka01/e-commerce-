package com.Springer.Gojo.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Springer.Gojo.Dto.Order.OrderResponse;
import com.Springer.Gojo.Dto.Payment.PaymentResponse;
import com.Springer.Gojo.entity.Cart;
import com.Springer.Gojo.entity.CartItem;
import com.Springer.Gojo.entity.Order;
import com.Springer.Gojo.entity.OrderItem;
import com.Springer.Gojo.entity.OrderStatus;
import com.Springer.Gojo.entity.Product;
import com.Springer.Gojo.entity.User;
import com.Springer.Gojo.exception.ResourceNotFoundException;
import com.Springer.Gojo.exception.UnauthorizedException;
import com.Springer.Gojo.mapper.OrderMapper;
import com.Springer.Gojo.repository.CartRepository;
import com.Springer.Gojo.repository.OrderRepository;
import com.Springer.Gojo.repository.UserRepository;
import com.Springer.Gojo.service.OrderService;
import com.Springer.Gojo.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;
	private final PaymentService paymentService;

	@Override
	@Transactional
	public OrderResponse placeOrder() {

		// 1. Find user in the database
		User user = getCurrentUser();

		// 2. Find user cart in the database
		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found."));

		// 3. Check if cart empty or not
		if (cart.getItems().isEmpty())
			throw new IllegalArgumentException("Cart is empty.");

		// 4. Validate stock and deduct it
		for (CartItem item : cart.getItems()) {
			Product product = item.getProduct();

			// validation is done internally in sell() method
			if (product.getStock() < item.getQuantity()) {
				throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
			}
		}

		for (CartItem item : cart.getItems()) {
			item.getProduct().sell(item.getQuantity());
		}

		// 5. Convert every cart item into order item
		List<OrderItem> orderItems = cart.getItems().stream()
				.map(cartItem -> OrderItem.createOrderItem(cartItem.getProduct(), cartItem.getQuantity())).toList();

		// 6. Create order for every orderItem
		Order order = Order.createOrder(user, orderItems, cart.getTotalPrice());

		// 7. Save created order
		Order savedOrder = orderRepository.save(order);

		// Create Payment
		PaymentResponse payment = paymentService.createPayment(savedOrder);
		
		// 8.Clear the cart because order is placed so there is nothing left in it
		cart.clear();
		cartRepository.save(cart);

		// Return response to user
		return orderMapper.toResponseWithPayment(savedOrder, payment.paymentId());
	}

	private User getCurrentUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found."));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<OrderResponse> getOrderHistory(Pageable pageable) {
		User user = getCurrentUser();

		// List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);

		// Pageable pageable = PageRequest.of(page, size,
		// Sort.by("orderDate").descending()); // Don't need it because we are using
		// object instead so now front-end can control the sorting and size

		Page<Order> orderPage = orderRepository.findByUser(user, pageable);

		return orderPage.map(orderMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public OrderResponse getOrderById(Long id) {
		User user = getCurrentUser();
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("order not found."));

		if (!order.getUser().getId().equals(user.getId())) {
			throw new RuntimeException("Access denied");
		}
		return orderMapper.toResponse(order);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<OrderResponse> getAllOrders(Pageable pageable) {
		Page<Order> order = orderRepository.findAll(pageable);

		return order.map(orderMapper::toResponse);
	}

	@Override
	@Transactional
	public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		order.updateStatus(status);
		Order updatedOrder = orderRepository.save(order);
		return orderMapper.toResponse(updatedOrder);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<OrderResponse> getOrdersByUserForAdmin(Long userId, Pageable pageable) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return orderRepository.findByUser(user, pageable).map(orderMapper::toResponse);
	}

	@Override
	@Transactional
	public OrderResponse cancelOrder(Long id) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		// check if current user owns this or
		User user = getCurrentUser();

		boolean isOwner = order.getUser().getId().equals(user.getId());
		boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

		if (!isOwner && !isAdmin) {
			throw new UnauthorizedException("You don't have permission to cancel this order");
		}

		order.cancelOrder();

		for (OrderItem item : order.getItems()) {
			Product product = item.getProduct();
			product.restock(item.getQuantity());
		}
		return orderMapper.toResponse(orderRepository.save(order));
	}
}
