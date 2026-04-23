package com.Springer.Gojo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Springer.Gojo.Dto.Payment.PaymentResponse;
import com.Springer.Gojo.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Gojo/user/order/payments")
@Tag(name = "Payment Controller", description = "Endpoints for processing transactions and handling gateway callbacks")
public class PaymentController {

	private final PaymentService paymentService;
	
	 @Operation(summary = "Process a payment", description = "Simulates a gateway interaction to confirm or fail a pending payment.")
	    @ApiResponse(responseCode = "200", description = "Payment processed (check status in body)")
	@PostMapping("{paymentId}/process")
	public ResponseEntity<PaymentResponse> processPayment(
			 @Parameter(description = "ID of the payment record") @PathVariable Long paymentId){
		return ResponseEntity.ok(paymentService.processPayment(paymentId));
	}
	
	 @Operation(summary = "Retry payment for an order", description = "Creates a new payment attempt for an existing order if previous attempts failed.")
	    @ApiResponse(responseCode = "409", description = "Order already paid or confirmed")
	@PostMapping("/retry/{orderId}")
	public ResponseEntity<PaymentResponse> retryPayment(
			@Parameter(description = "ID of the order to retry payment for") @PathVariable Long orderId){
		return ResponseEntity.ok(paymentService.retryPayment(orderId));
	}
	
	 @Operation(summary = "Payment Webhook Handler", description = "Callback endpoint for external payment providers (Razorpay/PhonePe) to update payment status.")
	    @ApiResponse(responseCode = "200", description = "Webhook acknowledged")
	@PostMapping("/webhook")
	public ResponseEntity<String> handleWebhook(
			@Parameter(description = "ID of the payment")@RequestParam Long paymentId, 
			@Parameter(description = "Result from the gateway") @RequestParam boolean success){
		paymentService.handleWebhook(paymentId, success);
		return ResponseEntity.ok("Webhook processed");
	}
}
