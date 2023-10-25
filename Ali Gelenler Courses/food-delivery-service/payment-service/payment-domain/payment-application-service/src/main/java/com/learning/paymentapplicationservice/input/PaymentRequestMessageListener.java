package com.learning.paymentapplicationservice.input;

import com.learning.paymentapplicationservice.command.PaymentRequest;

public interface PaymentRequestMessageListener {

	void completePayment(PaymentRequest paymentRequest);

	void cancelPayment(PaymentRequest paymentRequest);
}
