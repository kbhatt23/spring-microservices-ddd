package com.learning.orderservice.core.events;

import java.time.ZonedDateTime;

import com.learning.commondomain.events.DomainEvent;
import com.learning.orderservice.core.entities.Order;

public abstract class OrderEvent implements DomainEvent<Order> {
	private final Order order;
	private final ZonedDateTime createdAt;

	protected OrderEvent(Order order, ZonedDateTime createdAt) {
		this.order = order;
		this.createdAt = createdAt;
	}

	public final Order getOrder() {
		return order;
	}

	public final ZonedDateTime getCreatedAt() {
		return createdAt;
	}
}
