package com.Springer.Gojo.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Table(name = "carts")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@Builder.Default
	private List<CartItem> items = new ArrayList<>();

	public void addItem(Product product, int quantity) {

		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be positive.");
		}
		// Check if product already in cart
		Optional<CartItem> existingItem = items.stream()
				.filter(item -> item.getProduct().getId().equals(product.getId())).findFirst();

		if (existingItem.isPresent()) {
			// Update quantity on existing item
			CartItem item = existingItem.get();
			item.updateQuantity(item.getQuantity() + quantity);
		} else {
			// Add brand new item
			CartItem newItem = new CartItem(this, product, quantity);
			this.items.add(newItem);
		}

	}

	public void removeItem(Product product) {
		items.removeIf(item -> item.getProduct().getId().equals(product.getId()));
	}

	public void clear() {
		items.clear();
	}

	public BigDecimal getTotalPrice() {
		return items.stream().map(CartItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
