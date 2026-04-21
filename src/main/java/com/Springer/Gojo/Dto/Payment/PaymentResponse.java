package com.Springer.Gojo.Dto.Payment;

import java.math.BigDecimal;

public record PaymentResponse(
		Long paymentId,
		Long orderId,
		BigDecimal amount,
		String status,
		String transactionId
		) {

}
