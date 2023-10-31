package com.learning.paymentapplicationservice.mapper;

import java.time.ZoneId;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.learning.commondomain.constants.CommonDomainConstants;
import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.OrderId;
import com.learning.paymentapplicationservice.command.PaymentRequest;
import com.learning.paymentapplicationservice.outbox.model.OrderEventPayload;
import com.learning.paymentdomaincore.entities.Payment;
import com.learning.paymentdomaincore.events.PaymentEvent;

@Component
public class PaymentDataMapper {

	public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
		return new Payment(null, new OrderId(UUID.fromString(paymentRequest.getOrderId())),
				new CustomerId(UUID.fromString(paymentRequest.getCustomerId())), new Money(paymentRequest.getPrice()),
				paymentRequest.getCreatedAt().atZone(ZoneId.of(CommonDomainConstants.UTC)));

	}

	public OrderEventPayload paymentEventToOrderEventPayload(PaymentEvent paymentEvent) {
		return OrderEventPayload.builder().paymentId(paymentEvent.getPayment().getId().getValue().toString())
				.customerId(paymentEvent.getPayment().getCustomerId().getValue().toString())
				.orderId(paymentEvent.getPayment().getOrderId().getValue().toString())
				.price(paymentEvent.getPayment().getPrice().getAmount()).createdAt(paymentEvent.getCreatedAt())
				.paymentStatus(paymentEvent.getPayment().getPaymentStatus().name())
				.failureMessages(paymentEvent.getFailureMessages()).build();
	}
}
