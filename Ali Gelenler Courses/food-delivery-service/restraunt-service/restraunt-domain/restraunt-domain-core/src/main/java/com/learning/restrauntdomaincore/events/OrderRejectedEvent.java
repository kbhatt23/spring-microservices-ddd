package com.learning.restrauntdomaincore.events;

import java.time.ZonedDateTime;
import java.util.List;

import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.restrauntdomaincore.entities.OrderApproval;

public class OrderRejectedEvent extends OrderApprovalEvent {

	public OrderRejectedEvent(OrderApproval orderApproval, RestrauntId restrauntId, List<String> failureMessages,
			ZonedDateTime createdAt) {
		super(orderApproval, restrauntId, failureMessages, createdAt);
	}

}
