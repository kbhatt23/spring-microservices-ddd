package com.learning.dataaccess.entity;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import com.learning.commondomain.valueobjects.OrderStatus;
import com.learning.outbox.OutboxStatus;
import com.learning.saga.SagaStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_outbox")
@Entity
public class PaymentOutboxEntity {

	@Id
	private UUID id;

	private UUID sagaId;

	private ZonedDateTime createdAt;
	
	private ZonedDateTime processedAt;
	
	private String type;
	
	private String payload;
	
	@Enumerated(EnumType.STRING)
	private SagaStatus sagaStatus;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@Enumerated(EnumType.STRING)
	private OutboxStatus outboxStatus;
	
	@Version
	private int version;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		PaymentOutboxEntity that = (PaymentOutboxEntity) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}