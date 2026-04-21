package com.Springer.Gojo.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Builder
@Table(name = "order_items")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long itemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	private String productName;
	private BigDecimal price;
	private int quantity;

	public BigDecimal getSubtotal() {
		return this.price.multiply(BigDecimal.valueOf(this.quantity));
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public static OrderItem createOrderItem(Product product, int quantity) {

		if (quantity <= 0)
			throw new IllegalArgumentException("Quantity must be positive");
		if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0)
			throw new IllegalArgumentException("Invalid Price");

		return OrderItem.builder().product(product).productName(product.getName()).price(product.getPrice())
				.quantity(quantity).build();
	}

}
