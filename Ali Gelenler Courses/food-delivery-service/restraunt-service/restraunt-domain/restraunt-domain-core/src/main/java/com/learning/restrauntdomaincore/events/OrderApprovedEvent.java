package com.learning.restrauntdomaincore.events;

import java.time.ZonedDateTime;
import java.util.List;

import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.restrauntdomaincore.entities.OrderApproval;

public class OrderApprovedEvent extends OrderApprovalEvent {

	public OrderApprovedEvent(OrderApproval orderApproval, RestrauntId restrauntId, List<String> failureMessages,
			ZonedDateTime createdAt) {
		super(orderApproval, restrauntId, failureMessages, createdAt);
	}

}
