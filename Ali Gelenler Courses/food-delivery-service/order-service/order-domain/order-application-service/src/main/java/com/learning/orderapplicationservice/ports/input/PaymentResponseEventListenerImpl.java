package com.learning.orderapplicationservice.ports.input;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.learning.commondomain.constants.CommonDomainConstants;
import com.learning.orderapplicationservice.external.PaymentResponse;
import com.learning.orderapplicationservice.saga.OrderPaymentSaga;

import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@Slf4j
public class PaymentResponseEventListenerImpl implements PaymentResponseEventListener {

	private final OrderPaymentSaga orderPaymentSaga;

	public PaymentResponseEventListenerImpl(OrderPaymentSaga orderPaymentSaga) {
		this.orderPaymentSaga = orderPaymentSaga;
	}

	@Override
	public void paymentApproved(PaymentResponse paymentResponse) {
		orderPaymentSaga.process(paymentResponse);
		
		log.info("Publishing OrderPaidEvent for order id: {}", paymentResponse.getOrderId());
	}

	@Override
	public void paymentCancelled(PaymentResponse paymentResponse) {

		orderPaymentSaga.rollback(paymentResponse);
		
		log.info("Order is roll backed for order id: {} with failure messages: {}",
                paymentResponse.getOrderId(),
                String.join(CommonDomainConstants.DELIMETER, paymentResponse.getErrors()));
	}

}
