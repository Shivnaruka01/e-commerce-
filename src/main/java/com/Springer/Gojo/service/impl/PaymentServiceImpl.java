package com.Springer.Gojo.service.impl;

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
import com.Springer.Gojo.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;

	@Override
	@Transactional
	public PaymentResponse createPayment(Order order) {
		
		// Log intent
		log.atInfo()
			.setMessage("Creating payment record for order")
			.addKeyValue("orderId", order.getId())
			.addKeyValue("amount", order.getTotalAmount())
			.log();

		//		PREVENT DOUBLE PAYMENT
		Optional<Payment> existingPayment = paymentRepository
				.findByOrder(order)
				.filter(p -> p.getStatus() == PaymentStatus.SUCCESS);

		if(existingPayment.isPresent()) {
			log.atWarn()
				.setMessage("Payment creation blocked: Order already paid")
				.addKeyValue("orderId", order.getId())
				.log();
			throw new IllegalStateException("Order already paid");
		}

		Payment payment = Payment.builder()
				.order(order)
				.amount(order.getTotalAmount())
				.status(PaymentStatus.PENDING)
				.createdAt(LocalDateTime.now()).build();
		
		Payment savedPayment = paymentRepository.save(payment);
		
		log.atDebug()
			.setMessage("Payment record initialized")
			.addKeyValue("paymentId", savedPayment.getId())
			.log();
		
		return mapToResponse(savedPayment);
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

		log.atInfo()
        .setMessage("Processing transaction")
        .addKeyValue("paymentId", paymentId)
        .log();
		
		boolean isSuccess = Math.random() > 0.3;

		if (isSuccess) {
			payment.markAsSuccess(UUID.randomUUID().toString());
			payment.getOrder().updateStatus(OrderStatus.CONFIRMED);
			
			log.atInfo()
            .setMessage("Transaction SUCCESSFUL")
            .addKeyValue("paymentId", paymentId)
            .addKeyValue("transactionId", payment.getTransactionId())
            .log();
		} else {
			payment.markAsFailed();
			log.atWarn()
            .setMessage("Transaction FAILED")
            .addKeyValue("paymentId", paymentId)
            .log();
		}
		return mapToResponse(paymentRepository.save(payment));
	}

	@Override
	@Transactional
	public PaymentResponse retryPayment(Long orderId) {
		
		log.atInfo()
			.setMessage("Payment retry initiated")
			.addKeyValue("orderId", orderId)
			.log();
		
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		// PREVENT DOUBLE PAYMENT
		Optional<Payment> existingPayment = paymentRepository
				.findByOrder(order)
				.filter(p -> p.getStatus() == PaymentStatus.SUCCESS);

		if (existingPayment.isPresent() || order.getStatus() == OrderStatus.CONFIRMED) {
	        log.atWarn()
	           .setMessage("Retry blocked: Order is already in a successful state")
	           .addKeyValue("orderId", orderId)
	           .addKeyValue("orderStatus", order.getStatus())
	           .log();
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
	@Transactional
	public void handleWebhook(Long paymentId, boolean success) {
		// Webhooks are triggered by external servers, 
        // so logging the source and payload is vital.
		log.atInfo()
			.setMessage("Received Payment Webhook")
			.addKeyValue("paymentId", paymentId)
			.addKeyValue("externalStatus", success ? "SUCCESS" : "FAILURE")
	        .log();
		
		Payment payment = paymentRepository
				.findById(paymentId)
				.orElseThrow(() -> {
	                log.atError()
	                	.setMessage("Webhook Error: Payment ID not found")
	                	.addKeyValue("id", paymentId)
	                	.log();
	                return new ResourceNotFoundException("Payment not found");
	            });

		if(payment.getStatus() != PaymentStatus.PENDING) {
			log.atDebug()
				.setMessage("Webhook ignored: Payment already processed")
				.addKeyValue("paymentId", paymentId)
	            .addKeyValue("currentStatus", payment.getStatus())
	            .log();
			return;
		}

		if(success) {
	        String txnId = UUID.randomUUID().toString();
			payment.markAsSuccess(UUID.randomUUID().toString());
			payment.getOrder().updateStatus(OrderStatus.CONFIRMED);
			
			log.atInfo()
				.setMessage("Webhook: Payment confirmed successfully")
				.addKeyValue("txnId", txnId)
				.addKeyValue("paymentId", paymentId)
				.log();
		}else {
			payment.markAsFailed();
			log.atWarn()
				.setMessage("Webhook: Payment marked as FAILED")
				.addKeyValue("paymentId", paymentId)
				.log();
		}
		paymentRepository.save(payment);
	}

}
