package com.learning.orderapplicationservice.ports.input;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.learning.commondomain.constants.CommonDomainConstants;
import com.learning.orderapplicationservice.external.RestrauntApprovalResponse;
import com.learning.orderapplicationservice.saga.OrderApprovalSaga;
import com.learning.orderservice.core.events.OrderCancelledEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@Slf4j
public class RestrauntApprovalEventListenerImpl implements RestrauntApprovalEventListener {
	private final OrderApprovalSaga orderApprovalSaga;

	public RestrauntApprovalEventListenerImpl(OrderApprovalSaga orderApprovalSaga) {
		this.orderApprovalSaga = orderApprovalSaga;
	}

	@Override
	public void orderApproved(RestrauntApprovalResponse restrauntApprovalResponse) {
		orderApprovalSaga.process(restrauntApprovalResponse);
		log.info("Order is approved for order id: {}", restrauntApprovalResponse.getOrderId());
	}

	@Override
	public void orderRejected(RestrauntApprovalResponse restrauntApprovalResponse) {
		OrderCancelledEvent orderCancelledEvent = orderApprovalSaga.rollback(restrauntApprovalResponse);

		log.info("Publishing order cancelled event for order id: {} with failure messages: {}",
                restrauntApprovalResponse.getOrderId(),
                String.join(CommonDomainConstants.DELIMETER, restrauntApprovalResponse.getFailureMessages()));
		
		orderCancelledEvent.fire();
	}

}
