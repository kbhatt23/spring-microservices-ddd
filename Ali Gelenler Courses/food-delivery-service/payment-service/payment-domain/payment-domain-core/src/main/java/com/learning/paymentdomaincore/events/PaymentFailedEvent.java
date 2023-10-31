package com.learning.paymentdomaincore.events;

import java.time.ZonedDateTime;
import java.util.List;

import com.learning.paymentdomaincore.entities.Payment;

public class PaymentFailedEvent extends PaymentEvent {

	public PaymentFailedEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages) {
		super(payment, createdAt, failureMessages);
	}

}
