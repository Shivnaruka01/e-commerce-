package com.Springer.Gojo.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Springer.Gojo.Dto.Payment.PaymentResponse;
import com.Springer.Gojo.entity.Order;
import com.Springer.Gojo.entity.OrderStatus;
import com.Springer.Gojo.entity.Payment;
import com.Springer.Gojo.entity.PaymentStatus;
import com.Springer.Gojo.exception.ResourceNotFoundException;
import com.Springer.Gojo.repository.OrderRepository;
import com.Springer.Gojo.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;

	@Override
	@Transactional
	public PaymentResponse createPayment(Order order) {

		//		PREVENT DOUBLE PAYMENT
		Optional<Payment> existingPayment = paymentRepository
				.findByOrder(order)
				.filter(p -> p.getStatus() == PaymentStatus.SUCCESS);

		if(existingPayment.isPresent()) throw new IllegalStateException("Order already paid");

		Payment payment = Payment.builder()
				.order(order)
				.amount(order.getTotalAmount())
				.status(PaymentStatus.PENDING)
				.createdAt(LocalDateTime.now()).build();
		return mapToResponse(paymentRepository.save(payment));
	}

	private PaymentResponse mapToResponse(Payment payment) {
		return new PaymentResponse(
				payment.getId(), 
				payment.getOrder().getId(), 
				payment.getAmount(),
				payment.getStatus().name(), 
				payment.getTransactionId());
	}

	@Override
	@Transactional
	public PaymentResponse processPayment(Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

		boolean isSuccess = Math.random() > 0.3;

		if (isSuccess) {
			payment.markAsSuccess(UUID.randomUUID().toString());
			payment.getOrder().updateStatus(OrderStatus.CONFIRMED);
		} else {
			payment.markAsFailed();
		}
		return mapToResponse(paymentRepository.save(payment));
	}

	@Override
	@Transactional
	public PaymentResponse retryPayment(Long orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		// PREVENT DOUBLE PAYMENT
		Optional<Payment> existingPayment = paymentRepository
				.findByOrder(order)
				.filter(p -> p.getStatus() == PaymentStatus.SUCCESS);

		if(existingPayment.isPresent()) throw new IllegalStateException("Order already paid");

		if (order.getStatus() == OrderStatus.CONFIRMED) {
			throw new IllegalStateException("Order already paid");
		}

		Payment newPayment = Payment.builder().order(order).amount(order.getTotalAmount()).status(PaymentStatus.PENDING)
				.createdAt(LocalDateTime.now()).build();
		return mapToResponse(paymentRepository.save(newPayment));
	}

	@Override
	public PaymentResponse getPayment(Long paymentId) {
		Payment payment = paymentRepository
				.findById(paymentId)
				.orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
		return mapToResponse(payment);
	}

	@Override
	public void handleWebhook(Long payementId, boolean success) {

		Payment payment = paymentRepository
				.findById(payementId)
				.orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

		if(payment.getStatus() != PaymentStatus.PENDING) return;

		if(success) {
			payment.markAsSuccess(UUID.randomUUID().toString());
			payment.getOrder().updateStatus(OrderStatus.CONFIRMED);
		}else {
			payment.markAsFailed();
		}
		paymentRepository.save(payment);
	}



}
