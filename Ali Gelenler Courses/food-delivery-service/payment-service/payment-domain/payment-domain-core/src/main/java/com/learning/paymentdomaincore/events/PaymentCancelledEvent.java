package com.learning.paymentdomaincore.events;

import java.time.ZonedDateTime;
import java.util.Collections;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.paymentdomaincore.entities.Payment;

public class PaymentCancelledEvent extends PaymentEvent {

	private final DomainEventPublisher<PaymentCancelledEvent> publisher;

	public PaymentCancelledEvent(Payment payment, ZonedDateTime createdAt,
			DomainEventPublisher<PaymentCancelledEvent> publisher) {
		super(payment, createdAt, Collections.emptyList());
		this.publisher = publisher;
	}

	@Override
	public void fire() {
		this.publisher.publish(this);
	}

}
