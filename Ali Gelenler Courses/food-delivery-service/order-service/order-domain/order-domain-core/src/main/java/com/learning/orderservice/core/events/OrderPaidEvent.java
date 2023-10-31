package com.learning.orderservice.core.events;

import java.time.ZonedDateTime;

import com.learning.orderservice.core.entities.Order;

public class OrderPaidEvent extends OrderEvent {

	public OrderPaidEvent(Order order, ZonedDateTime createdAt) {
		super(order, createdAt);
	}

	// can add its own custom fields and methods and setter getters etc
}
