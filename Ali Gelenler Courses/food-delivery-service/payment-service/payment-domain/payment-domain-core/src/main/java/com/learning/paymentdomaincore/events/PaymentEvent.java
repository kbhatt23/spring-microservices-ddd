package com.learning.paymentdomaincore.events;

import java.time.ZonedDateTime;
import java.util.List;

import com.learning.commondomain.events.DomainEvent;
import com.learning.paymentdomaincore.entities.Payment;

public abstract class PaymentEvent implements DomainEvent<Payment> {

	private final Payment payment;
	private final ZonedDateTime createdAt;
	private final List<String> failureMessages;

	protected PaymentEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages) {
		this.payment = payment;
		this.createdAt = createdAt;
		this.failureMessages = failureMessages;
	}

	public Payment getPayment() {
		return payment;
	}

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	public List<String> getFailureMessages() {
		return failureMessages;
	}

}
