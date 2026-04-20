package com.Springer.Gojo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
	
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	private String transactionId;	// for payment gateway
	
	private LocalDateTime createdAt;
	
	public void markAsSuccess(String transactionId) {
		if(this.status != PaymentStatus.PENDING) {
			throw new IllegalStateException("Payment already processed");
		}
		this.status = PaymentStatus.SUCCESS;
		this.transactionId = transactionId;
	}
	
	public void markAsFailed() {
		if(this.status != PaymentStatus.PENDING) {
			throw new IllegalStateException("payment already processed");
		}
		this.status = PaymentStatus.FAILED;
	}
}
