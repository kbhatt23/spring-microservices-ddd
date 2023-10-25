package com.learning.orderservice.core.services;

import java.util.List;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.orderservice.core.entities.Order;
import com.learning.orderservice.core.entities.Restraunt;
import com.learning.orderservice.core.events.OrderCancelledEvent;
import com.learning.orderservice.core.events.OrderCreatedEvent;
import com.learning.orderservice.core.events.OrderPaidEvent;

//let domain service use entity,aggregates and data layer to save in db + run logic + create domain event
//let application service use this domain even interact with kafka producer and send the event to topic
public interface OrderDomainService {

	OrderCreatedEvent validateAndInitializeOrder(Order order, Restraunt restraunt,DomainEventPublisher<OrderCreatedEvent> publisher);
	
	OrderPaidEvent payOrder(Order order, DomainEventPublisher<OrderPaidEvent> publisher);
	
	void approveOrder(Order order);
	
	OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessage, DomainEventPublisher<OrderCancelledEvent> publisher);
	
	void cancelOrder(Order order, List<String> failureMessage);
}
