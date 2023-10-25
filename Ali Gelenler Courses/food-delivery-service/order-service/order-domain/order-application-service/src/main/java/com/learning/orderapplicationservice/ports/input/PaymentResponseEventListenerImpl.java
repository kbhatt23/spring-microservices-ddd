package com.learning.orderapplicationservice.ports.input;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.learning.orderapplicationservice.external.PaymentResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@Slf4j
public class PaymentResponseEventListenerImpl implements PaymentResponseEventListener {

	@Override
	public void paymentApproved(PaymentResponse paymentResponse) {

	}

	@Override
	public void paymentCancelled(PaymentResponse paymentResponse) {

	}

}
