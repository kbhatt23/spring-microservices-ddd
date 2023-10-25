package com.learning.restrauntdomaincore.entities;

import com.learning.commondomain.entities.BaseEntity;
import com.learning.commondomain.valueobjects.OrderApprovalStatus;
import com.learning.commondomain.valueobjects.OrderId;
import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.restrauntdomaincore.valueobjects.OrderApprovalId;

public class OrderApproval extends BaseEntity<OrderApprovalId> {

	private final RestrauntId restrauntId;

	private final OrderId orderId;

	private final OrderApprovalStatus orderApprovalStatus;

	public OrderApproval(OrderApprovalId id, RestrauntId restrauntId, OrderId orderId,
			OrderApprovalStatus orderApprovalStatus) {
		super(id);
		this.restrauntId = restrauntId;
		this.orderId = orderId;
		this.orderApprovalStatus = orderApprovalStatus;
	}

	public RestrauntId getRestrauntId() {
		return restrauntId;
	}

	public OrderId getOrderId() {
		return orderId;
	}

	public OrderApprovalStatus getOrderApprovalStatus() {
		return orderApprovalStatus;
	}

	
}
