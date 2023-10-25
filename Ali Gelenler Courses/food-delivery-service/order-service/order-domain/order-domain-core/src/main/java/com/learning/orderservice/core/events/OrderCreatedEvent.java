package com.learning.orderservice.core.events;

import java.time.ZonedDateTime;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.orderservice.core.entities.Order;

public class OrderCreatedEvent extends OrderEvent{
	
	private final DomainEventPublisher<OrderCreatedEvent> publisher;

	public OrderCreatedEvent(Order order, ZonedDateTime createdAt,DomainEventPublisher<OrderCreatedEvent> publisher) {
		super(order, createdAt);
		this.publisher=publisher;
	}
	
	@Override
	public void fire() {
		this.publisher.publish(this);
	}
}
