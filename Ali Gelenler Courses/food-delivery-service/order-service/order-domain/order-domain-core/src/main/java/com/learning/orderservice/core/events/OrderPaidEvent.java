package com.learning.orderservice.core.events;

import java.time.ZonedDateTime;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.orderservice.core.entities.Order;

public class OrderPaidEvent extends OrderEvent {

	private final DomainEventPublisher<OrderPaidEvent> publisher;

	public OrderPaidEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<OrderPaidEvent> publisher) {
		super(order, createdAt);
		this.publisher = publisher;
	}

	// can add its own custom fields and methods and setter getters etc

	@Override
	public void fire() {
		this.publisher.publish(this);
	}
}
