package com.learning.paymentdomaincore.entities;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.learning.commondomain.constants.CommonDomainConstants;
import com.learning.commondomain.entities.AggregateRoot;
import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.OrderId;
import com.learning.commondomain.valueobjects.PaymentStatus;
import com.learning.paymentdomaincore.valueobjects.PaymentId;

public class Payment extends AggregateRoot<PaymentId> {

	private final OrderId orderId;
	private final CustomerId customerId;
	private final Money price;

	private PaymentStatus paymentStatus;
	private ZonedDateTime createdAt;

	public Payment(PaymentId id, OrderId orderId, CustomerId customerId, Money price) {
		super(id);
		this.orderId = orderId;
		this.customerId = customerId;
		this.price = price;
	}

	public void initializePayment() {
		setId(new PaymentId(UUID.randomUUID()));
		createdAt = ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC));
	}

	public void validatePayment(List<String> failureMessages) {
		if (price == null || !price.isGreaterThanZero()) {
			failureMessages.add("Total price must be greater than zero!");
		}
	}

	public void updateStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	public OrderId getOrderId() {
		return orderId;
	}

	public CustomerId getCustomerId() {
		return customerId;
	}

	public Money getPrice() {
		return price;
	}

}
