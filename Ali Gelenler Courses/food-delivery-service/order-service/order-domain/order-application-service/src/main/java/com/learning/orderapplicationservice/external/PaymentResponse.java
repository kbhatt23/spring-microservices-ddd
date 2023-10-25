package com.learning.orderapplicationservice.external;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.learning.commondomain.valueobjects.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PaymentResponse {

	private String id;
	private String sagaId;
	private String paymentId;
	private String orderId;
	private String customerId;
	private BigDecimal price;
	private Instant createdAt;
	private PaymentStatus paymentStatus;
	private List<String> errors;
}
