package com.learning.restrauntdomaincore.events;

import java.time.ZonedDateTime;
import java.util.List;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.restrauntdomaincore.entities.OrderApproval;

public class OrderRejectedEvent extends OrderApprovalEvent {

	private final DomainEventPublisher<OrderRejectedEvent> orderRejectedEventPublisher;

	public OrderRejectedEvent(OrderApproval orderApproval, RestrauntId restrauntId, List<String> failureMessages,
			ZonedDateTime createdAt, DomainEventPublisher<OrderRejectedEvent> orderRejectedEventPublisher) {
		super(orderApproval, restrauntId, failureMessages, createdAt);
		this.orderRejectedEventPublisher = orderRejectedEventPublisher;
	}

	@Override
	public void fire() {
		orderRejectedEventPublisher.publish(this);
	}

}
