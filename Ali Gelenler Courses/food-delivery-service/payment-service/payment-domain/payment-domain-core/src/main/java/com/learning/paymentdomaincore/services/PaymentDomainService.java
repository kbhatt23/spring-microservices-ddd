package com.learning.paymentdomaincore.services;

import java.util.List;

import com.learning.paymentdomaincore.entities.CreditEntry;
import com.learning.paymentdomaincore.entities.CreditHistory;
import com.learning.paymentdomaincore.entities.Payment;
import com.learning.paymentdomaincore.events.PaymentEvent;

public interface PaymentDomainService {

	public PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
			List<CreditHistory> creditHistories, List<String> failureMessages);

	public PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
			List<CreditHistory> creditHistories, List<String> failureMessages);

}
