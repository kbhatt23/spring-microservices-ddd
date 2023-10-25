package com.learning.paymentdomaincore.events;

import java.time.ZonedDateTime;
import java.util.Collections;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.paymentdomaincore.entities.Payment;

public class PaymentCompletedEvent extends PaymentEvent {

	private final DomainEventPublisher<PaymentCompletedEvent> publisher;

	public PaymentCompletedEvent(Payment payment, ZonedDateTime createdAt,
			DomainEventPublisher<PaymentCompletedEvent> publisher) {
		super(payment, createdAt, Collections.emptyList());
		this.publisher = publisher;
	}

	@Override
	public void fire() {
		this.publisher.publish(this);
	}

}
