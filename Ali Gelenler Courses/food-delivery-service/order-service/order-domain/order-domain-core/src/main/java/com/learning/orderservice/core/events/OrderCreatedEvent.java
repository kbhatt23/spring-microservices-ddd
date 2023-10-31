package com.learning.orderservice.core.events;

import java.time.ZonedDateTime;

import com.learning.orderservice.core.entities.Order;

public class OrderCreatedEvent extends OrderEvent{
	

	public OrderCreatedEvent(Order order, ZonedDateTime createdAt) {
		super(order, createdAt);
	}
	
}
