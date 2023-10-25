package com.learning.restrauntdomaincore.services;

import java.util.List;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.restrauntdomaincore.entities.Restraunt;
import com.learning.restrauntdomaincore.events.OrderApprovalEvent;
import com.learning.restrauntdomaincore.events.OrderApprovedEvent;
import com.learning.restrauntdomaincore.events.OrderRejectedEvent;

public interface RestrauntDomainService {

	public OrderApprovalEvent validateOrder(Restraunt restraunt, List<String> failureMessages,
			DomainEventPublisher<OrderApprovedEvent> orderApprovedEventPublisher,
			DomainEventPublisher<OrderRejectedEvent> orderRejectedEventPublisher);
}
