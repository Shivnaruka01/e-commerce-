package com.Springer.Gojo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Springer.Gojo.Dto.Order.OrderResponse;
import com.Springer.Gojo.entity.OrderStatus;
import com.Springer.Gojo.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("Gojo/orders")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponse> placeOrder() {
		return ResponseEntity.ok(orderService.placeOrder());
	}

	@GetMapping
	// public ResponseEntity<List<OrderResponse>> getMyOrders(){
	// return ResponseEntity.ok(orderService.getOrderHistory());
	// }
	public ResponseEntity<Page<OrderResponse>> getMyOrders(
			@PageableDefault(page = 0, size = 10, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(orderService.getOrderHistory(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.getOrderById(id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin")
	public ResponseEntity<Page<OrderResponse>> getAllOrders(
			@PageableDefault(page = 0, size = 10, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(orderService.getAllOrders(pageable));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/admin/{id}/status")
	public ResponseEntity<OrderResponse> updatedOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
		return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin/user/{userId}")
	public ResponseEntity<Page<OrderResponse>> getOrderByUserId(@PathVariable Long userId,
			@PageableDefault(size = 10, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(orderService.getOrdersByUserForAdmin(userId, pageable));
	}

	@PostMapping("/{id}/cancel")
	public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.cancelOrder(id));
	}
}
