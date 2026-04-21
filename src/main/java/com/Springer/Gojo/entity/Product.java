package com.Springer.Gojo.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA required, hidden from app logic
@AllArgsConstructor(access = AccessLevel.PRIVATE) // For Builder use only
@Table(name = "products")
@Builder
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;

	private int stock;

	// Static factory method for controlled creation
	public static Product createProduct(String name, String description, BigDecimal price, int initialStock) {

		if (price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Initial price cannot be negative");
		}
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Product name cannot be empty");
		}
		if (description == null || description.isBlank()) {
			throw new IllegalArgumentException("Description cannot be empty");
		}
		return Product.builder().name(name).description(description).price(price).stock(Math.max(initialStock, 0)) // Ensure
																													// stock
																													// isn't
																													// negative
				.build();
	}

	// Update price with validation
	public void adjustPrice(BigDecimal newPrice) {
		if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Price must be greater than zero");
		}
		this.price = newPrice;
	}

	public void restock(int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("Restock quantity must be positive.");
		}
		this.stock += quantity;
	}

	public void sell(int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be positive");
		}
		if (quantity > this.stock) {
			throw new IllegalArgumentException("Not enough stock available");
		}
		this.stock -= quantity;
	}

	public void updateDetails(String name, String description) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Product name cannot be empty");
		}
		this.name = name;
		this.description = description;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + ", stock="
				+ stock + "]";
	}

}
