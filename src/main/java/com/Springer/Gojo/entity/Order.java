package com.Springer.Gojo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "orders")
@Builder
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	private BigDecimal totalAmount;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@Builder.Default
	private List<OrderItem> items = new ArrayList<>();

	public static Order createOrder(User user, List<OrderItem> items, BigDecimal totalAmount) {
		Order order = Order.builder().user(user).items(items).totalAmount(totalAmount).status(OrderStatus.CREATED)
				.orderDate(LocalDateTime.now()).items(items).build();

		items.forEach(item -> item.setOrder(order));
		return order;
	}

	public void updateStatus(OrderStatus newStatus) {
		if (this.status == OrderStatus.DELIVERED)
			throw new IllegalArgumentException("Cannot change status of a dilivered order");

		this.status = newStatus;
	}

	public void cancelOrder() {
		if (this.status == OrderStatus.CANCELLED)
			throw new IllegalStateException("Order is already cancelled.");

		if (this.status == OrderStatus.SHIPPED)
			throw new IllegalStateException(
					"Order is currently in transit (SHIPPED) and cannot be cancelled. Please wait for delivery to initiate a return/cancel.");

		this.status = OrderStatus.CANCELLED;
	}
}
