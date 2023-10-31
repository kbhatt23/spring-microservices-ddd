package com.learning.restrauntdataaccess.entities;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.learning.commondomain.valueobjects.OrderApprovalStatus;
import com.learning.outbox.OutboxStatus;

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
@Table(name = "order_outbox")
@Entity
public class OrderOutboxEntity {

	@Id
	private UUID id;
	
	private UUID sagaId;
	
	private ZonedDateTime createdAt;
	
	private ZonedDateTime processedAt;
	
	private String type;
	
	private String payload;
	
	@Enumerated(EnumType.STRING)
	private OutboxStatus outboxStatus;
	
	@Enumerated(EnumType.STRING)
	private OrderApprovalStatus approvalStatus;
	
	@Version
	private int version;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OrderOutboxEntity that = (OrderOutboxEntity) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}