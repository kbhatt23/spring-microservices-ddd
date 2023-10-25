package com.learning.orderapplicationservice.ports.input;

import com.learning.orderapplicationservice.external.PaymentResponse;

public interface PaymentResponseEventListener {

	void paymentApproved(PaymentResponse paymentResponse);
	
	void paymentCancelled(PaymentResponse paymentResponse);
}
