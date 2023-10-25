package com.learning.paymentdomaincore.services;

import java.util.List;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.paymentdomaincore.entities.CreditEntry;
import com.learning.paymentdomaincore.entities.CreditHistory;
import com.learning.paymentdomaincore.entities.Payment;
import com.learning.paymentdomaincore.events.PaymentCancelledEvent;
import com.learning.paymentdomaincore.events.PaymentCompletedEvent;
import com.learning.paymentdomaincore.events.PaymentEvent;
import com.learning.paymentdomaincore.events.PaymentFailedEvent;

public interface PaymentDomainService {

	public PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
			List<CreditHistory> creditHistories, List<String> failureMessages,
			DomainEventPublisher<PaymentCompletedEvent> paymentCompleteDomainEventPublisher,
			DomainEventPublisher<PaymentFailedEvent> paymentFailedDomainEventPublisher);

	public PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
			List<CreditHistory> creditHistories, List<String> failureMessages,
			DomainEventPublisher<PaymentCancelledEvent> paymentCancelledDomainEventPublisher,
			DomainEventPublisher<PaymentFailedEvent> paymentFailedDomainEventPublisher);

}
