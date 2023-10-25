package com.learning.paymentdomaincore.events;

import java.time.ZonedDateTime;
import java.util.List;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.paymentdomaincore.entities.Payment;

public class PaymentFailedEvent extends PaymentEvent {

	private final DomainEventPublisher<PaymentFailedEvent> publisher;

	public PaymentFailedEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages,
			DomainEventPublisher<PaymentFailedEvent> publisher) {
		super(payment, createdAt, failureMessages);
		this.publisher = publisher;
	}

	@Override
	public void fire() {
		this.publisher.publish(this);
	}

}
