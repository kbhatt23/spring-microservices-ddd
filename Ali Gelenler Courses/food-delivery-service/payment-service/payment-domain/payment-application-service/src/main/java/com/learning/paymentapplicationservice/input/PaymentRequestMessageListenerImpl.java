package com.learning.paymentapplicationservice.input;

import org.springframework.stereotype.Service;

import com.learning.paymentapplicationservice.command.PaymentRequest;
import com.learning.paymentapplicationservice.helper.PaymentRequestHelper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {

	private final PaymentRequestHelper paymentRequestHelper;

	public PaymentRequestMessageListenerImpl(PaymentRequestHelper paymentRequestHelper) {
		this.paymentRequestHelper = paymentRequestHelper;
	}

	@Override
	public void completePayment(PaymentRequest paymentRequest) {
		paymentRequestHelper.persistPayment(paymentRequest);
		// let helper also send message to kafka via publisher
	}

	@Override
	public void cancelPayment(PaymentRequest paymentRequest) {
		paymentRequestHelper.persistCancelPayment(paymentRequest);
	}
}
