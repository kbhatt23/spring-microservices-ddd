package com.learning.restrauntdomaincore.entities;

import java.util.List;
import java.util.UUID;

import com.learning.commondomain.entities.AggregateRoot;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.OrderApprovalStatus;
import com.learning.commondomain.valueobjects.OrderStatus;
import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.restrauntdomaincore.valueobjects.OrderApprovalId;

public class Restraunt extends AggregateRoot<RestrauntId> {

	private OrderApproval orderApproval;
	private boolean active;
	private final OrderDetail orderDetail;

	public Restraunt(RestrauntId id, OrderApproval orderApproval, boolean active, OrderDetail orderDetail) {
		super(id);
		this.orderApproval = orderApproval;
		this.active = active;
		this.orderDetail = orderDetail;
	}

	public void validateOrder(List<String> failureMessages) {
		if (orderDetail.getOrderStatus() != OrderStatus.PAID) {
			failureMessages.add("Payment is not completed for order: " + orderDetail.getId());
		}

		Money totalAmount = orderDetail.getProducts().stream().map(product -> {
			if (!product.isAvailable()) {
				failureMessages.add("Product with id: " + product.getId().getValue() + " is not available");
			}
			return product.getPrice().multiply(product.getQuantity());
		}).reduce(Money.ZERO_MONEY, Money::add);

		if (!totalAmount.equals(orderDetail.getTotalAmount()))
			failureMessages.add("Price total is not correct for order: " + orderDetail.getId());
	}

	public void constructOrderApproval(OrderApprovalStatus orderApprovalStatus) {
		this.orderApproval = new OrderApproval(new OrderApprovalId(UUID.randomUUID()), getId(),
				getOrderDetail().getId(), orderApprovalStatus);
	}

	public OrderApproval getOrderApproval() {
		return orderApproval;
	}

	public void setOrderApproval(OrderApproval orderApproval) {
		this.orderApproval = orderApproval;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public OrderDetail getOrderDetail() {
		return orderDetail;
	}

}
