package com.Springer.Gojo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Springer.Gojo.Dto.Payment.PaymentResponse;
import com.Springer.Gojo.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Gojo/user/order/payments")
public class PaymentController {

	private final PaymentService paymentService;
	
	@PostMapping("{paymentId}/process")
	public ResponseEntity<PaymentResponse> processPayment(@PathVariable Long paymentId){
		return ResponseEntity.ok(paymentService.processPayment(paymentId));
	}
	
	@PostMapping("/retry/{orderId}")
	public ResponseEntity<PaymentResponse> retryPayment(@PathVariable Long orderId){
		return ResponseEntity.ok(paymentService.retryPayment(orderId));
	}
	
	@PostMapping("/webhook")
	public ResponseEntity<String> handleWebhook(
			@RequestParam Long paymentId, @RequestParam boolean success){
		paymentService.handleWebhook(paymentId, success);
		return ResponseEntity.ok("Webhook processed");
	}
}
