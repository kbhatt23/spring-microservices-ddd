package com.learning.restrauntdomaincore.events;

import java.time.ZonedDateTime;
import java.util.List;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.restrauntdomaincore.entities.OrderApproval;

public class OrderApprovedEvent extends OrderApprovalEvent {

	private final DomainEventPublisher<OrderApprovedEvent> orderApprovedEventPublisher;

	public OrderApprovedEvent(OrderApproval orderApproval, RestrauntId restrauntId, List<String> failureMessages,
			ZonedDateTime createdAt, DomainEventPublisher<OrderApprovedEvent> orderApprovedEventPublisher) {
		super(orderApproval, restrauntId, failureMessages, createdAt);
		this.orderApprovedEventPublisher = orderApprovedEventPublisher;
	}

	@Override
	public void fire() {
		orderApprovedEventPublisher.publish(this);
	}

}
