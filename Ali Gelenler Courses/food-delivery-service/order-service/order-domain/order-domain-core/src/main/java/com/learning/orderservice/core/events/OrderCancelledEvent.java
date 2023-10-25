package com.learning.orderservice.core.events;

import java.time.ZonedDateTime;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.orderservice.core.entities.Order;

public class OrderCancelledEvent extends OrderEvent {

	private final DomainEventPublisher<OrderCancelledEvent> publisher;

	public OrderCancelledEvent(Order order, ZonedDateTime createdAt,
			DomainEventPublisher<OrderCancelledEvent> publisher) {
		super(order, createdAt);
		this.publisher=publisher;
	}
	
	@Override
	public void fire() {
		this.publisher.publish(this);
	} 

}
