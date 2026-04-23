package com.Springer.Gojo.Dto.Payment;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public record PaymentResponse(
		@Schema(example = "901") Long paymentId,
		@Schema(example = "501") Long orderId,
		@Schema(example = "2400.00") BigDecimal amount,
		@Schema(example = "SUCCESS") String status,
		@Schema(example = "8273645") String transactionId
		) {}
