package com.learning.orderservice.core.events;

import java.time.ZonedDateTime;

import com.learning.orderservice.core.entities.Order;

public class OrderCancelledEvent extends OrderEvent {

	public OrderCancelledEvent(Order order, ZonedDateTime createdAt) {
		super(order, createdAt);
	}
	
}
