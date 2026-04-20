package com.Springer.Gojo.service;

import com.Springer.Gojo.Dto.Payment.PaymentResponse;
import com.Springer.Gojo.entity.Order;

public interface PaymentService {
	PaymentResponse createPayment(Order order);
	PaymentResponse processPayment(Long paymentId);
	PaymentResponse retryPayment(Long orderId);
	PaymentResponse getPayment(Long paymentId);
	void handleWebhook(Long payementId, boolean success);
}
