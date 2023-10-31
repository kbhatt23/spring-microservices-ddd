package com.learning.orderapplicationservice.oubox.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.learning.commondomain.valueobjects.OrderStatus;
import com.learning.outbox.OutboxStatus;
import com.learning.saga.SagaStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderPaymentOutboxMessage {
	private UUID id;
	private UUID sagaId;
	private ZonedDateTime createdAt;
	private ZonedDateTime processedAt;
	private String type;
	private String payload;
	private SagaStatus sagaStatus;
	private OrderStatus orderStatus;
	private OutboxStatus outboxStatus;
	private int version;

	public void setProcessedAt(ZonedDateTime processedAt) {
		this.processedAt = processedAt;
	}

	public void setSagaStatus(SagaStatus sagaStatus) {
		this.sagaStatus = sagaStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setOutboxStatus(OutboxStatus outboxStatus) {
		this.outboxStatus = outboxStatus;
	}
}