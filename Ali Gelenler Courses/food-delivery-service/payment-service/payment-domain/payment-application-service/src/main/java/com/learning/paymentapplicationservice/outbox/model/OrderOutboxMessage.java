package com.learning.paymentapplicationservice.outbox.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.learning.commondomain.valueobjects.PaymentStatus;
import com.learning.outbox.OutboxStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderOutboxMessage {
	private UUID id;
	
	private UUID sagaId;
	
	private ZonedDateTime createdAt;
	
	private ZonedDateTime processedAt;
	
	private String type;
	
	private String payload;
	
	private PaymentStatus paymentStatus;
	
	private OutboxStatus outboxStatus;
	
	private int version;

	public void setOutboxStatus(OutboxStatus outboxStatus) {
		this.outboxStatus = outboxStatus;
	}
}