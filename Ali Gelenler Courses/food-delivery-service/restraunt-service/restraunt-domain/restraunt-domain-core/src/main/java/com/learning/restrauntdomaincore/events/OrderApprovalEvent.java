package com.learning.restrauntdomaincore.events;

import java.time.ZonedDateTime;
import java.util.List;

import com.learning.commondomain.events.DomainEvent;
import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.restrauntdomaincore.entities.OrderApproval;

public abstract class OrderApprovalEvent implements DomainEvent<OrderApproval> {

	private final OrderApproval orderApproval;

	private RestrauntId restrauntId;

	private final List<String> failureMessages;

	private final ZonedDateTime createdAt;

	protected OrderApprovalEvent(OrderApproval orderApproval, RestrauntId restrauntId, List<String> failureMessages,
			ZonedDateTime createdAt) {
		this.orderApproval = orderApproval;
		this.restrauntId = restrauntId;
		this.failureMessages = failureMessages;
		this.createdAt = createdAt;
	}

	public RestrauntId getRestrauntId() {
		return restrauntId;
	}

	public void setRestrauntId(RestrauntId restrauntId) {
		this.restrauntId = restrauntId;
	}

	public OrderApproval getOrderApproval() {
		return orderApproval;
	}

	public List<String> getFailureMessages() {
		return failureMessages;
	}

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

}
