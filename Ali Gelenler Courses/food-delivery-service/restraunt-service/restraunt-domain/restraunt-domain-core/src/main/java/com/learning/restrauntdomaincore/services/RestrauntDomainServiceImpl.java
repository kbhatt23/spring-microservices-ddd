package com.learning.restrauntdomaincore.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.learning.commondomain.constants.CommonDomainConstants;
import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.commondomain.valueobjects.OrderApprovalStatus;
import com.learning.restrauntdomaincore.entities.Restraunt;
import com.learning.restrauntdomaincore.events.OrderApprovalEvent;
import com.learning.restrauntdomaincore.events.OrderApprovedEvent;
import com.learning.restrauntdomaincore.events.OrderRejectedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestrauntDomainServiceImpl implements RestrauntDomainService {

	@Override
	public OrderApprovalEvent validateOrder(Restraunt restraunt, List<String> failureMessages,
			DomainEventPublisher<OrderApprovedEvent> orderApprovedEventPublisher,
			DomainEventPublisher<OrderRejectedEvent> orderRejectedEventPublisher) {
		log.info("Validating order with id: {}", restraunt.getOrderDetail().getId().getValue());
		restraunt.validateOrder(failureMessages);

		ZonedDateTime currentZonedDateTime = ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC));
		OrderApprovalEvent orderApprovalEvent = null;
		if (failureMessages.isEmpty()) {
			log.info("Order is approved for order id: {}", restraunt.getOrderDetail().getId().getValue());
			restraunt.constructOrderApproval(OrderApprovalStatus.APPROVED);
			orderApprovalEvent = new OrderApprovedEvent(restraunt.getOrderApproval(), restraunt.getId(),
					failureMessages, currentZonedDateTime, orderApprovedEventPublisher);
		} else {
			log.info("Order is rejected for order id: {}", restraunt.getOrderDetail().getId().getValue());
			restraunt.constructOrderApproval(OrderApprovalStatus.REJECTED);
			orderApprovalEvent = new OrderRejectedEvent(restraunt.getOrderApproval(), restraunt.getId(),
					failureMessages, currentZonedDateTime, orderRejectedEventPublisher);
		}

		return orderApprovalEvent;
	}

}
