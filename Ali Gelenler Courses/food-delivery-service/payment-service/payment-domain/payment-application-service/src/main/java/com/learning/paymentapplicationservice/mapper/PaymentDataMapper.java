package com.learning.paymentapplicationservice.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.OrderId;
import com.learning.paymentapplicationservice.command.PaymentRequest;
import com.learning.paymentdomaincore.entities.Payment;

@Component
public class PaymentDataMapper {

	public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
		return new Payment(null, new OrderId(UUID.fromString(paymentRequest.getOrderId())),
				new CustomerId(UUID.fromString(paymentRequest.getCustomerId())), new Money(paymentRequest.getPrice()));

	}
}
