package com.learning.paymentapplicationservice.input;

import org.springframework.stereotype.Service;

import com.learning.paymentapplicationservice.command.PaymentRequest;
import com.learning.paymentapplicationservice.helper.PaymentRequestHelper;
import com.learning.paymentdomaincore.events.PaymentEvent;

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
		PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequest);
		//let helper also send message to kafka via publisher
		fire(paymentEvent);
		
	}

	@Override
	public void cancelPayment(PaymentRequest paymentRequest) {
		PaymentEvent paymentEvent = paymentRequestHelper.persistCancelPayment(paymentRequest);
		//let helper also send message to kafka via publisher
		fire(paymentEvent);
	}
	
	private void fire(PaymentEvent paymentEvent) {
		log.info("Publishing payment event with payment id: {} and order id: {}",
                paymentEvent.getPayment().getId().getValue(),
                paymentEvent.getPayment().getOrderId().getValue());
		paymentEvent.fire();
	}

}
