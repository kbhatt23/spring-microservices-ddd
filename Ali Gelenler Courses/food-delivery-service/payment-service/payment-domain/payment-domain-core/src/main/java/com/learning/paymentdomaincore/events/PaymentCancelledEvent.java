package com.learning.paymentdomaincore.events;

import java.time.ZonedDateTime;
import java.util.Collections;

import com.learning.paymentdomaincore.entities.Payment;

public class PaymentCancelledEvent extends PaymentEvent {

	public PaymentCancelledEvent(Payment payment, ZonedDateTime createdAt) {
		super(payment, createdAt, Collections.emptyList());
	}

}
