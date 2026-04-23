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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("Gojo/orders")
@Tag(name = "Order Conttroller", description = "Manage customer orders and admin order tracking")
public class OrderController {

	private final OrderService orderService;

	@Operation(summary = "Place a new order", description = "Converts cart items to orders and triggers payment creation")
	@PostMapping
	public ResponseEntity<OrderResponse> placeOrder() {
		return ResponseEntity.ok(orderService.placeOrder());
	}

	@Operation(summary = "Fetch orders", description = "Get all orders in past through paginated list")
	@GetMapping
	// public ResponseEntity<List<OrderResponse>> getMyOrders(){
	// return ResponseEntity.ok(orderService.getOrderHistory());
	// }
	public ResponseEntity<Page<OrderResponse>> getMyOrders(
			@PageableDefault(page = 0, size = 10, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(orderService.getOrderHistory(pageable));
	}

	@Operation(summary = "Fetch specifi order", description = "Get detail of specific order using its id")
	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.getOrderById(id));
	}

    @Operation(summary = "Get admin view of all orders", description = "Admin only. Fetches paginated list of all system orders")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin")
	public ResponseEntity<Page<OrderResponse>> getAllOrders(
			@PageableDefault(page = 0, size = 10, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(orderService.getAllOrders(pageable));
	}

    @Operation(summary = "Update order status", description = "Admin only. Use to change the status of order")
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/admin/{id}/status")
	public ResponseEntity<OrderResponse> updatedOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
		return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
	}

    @Operation(summary = "Order detail by id", description = "Admin only. Admin can find any order from the orders using order id")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin/user/{userId}")
	public ResponseEntity<Page<OrderResponse>> getOrderByUserId(@PathVariable Long userId,
			@PageableDefault(size = 10, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(orderService.getOrdersByUserForAdmin(userId, pageable));
	}

    @Operation(summary = "Cancel order", description = "Order can be cancel using that order id number")
	@PostMapping("/{id}/cancel")
	public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.cancelOrder(id));
	}
}
