package com.learning.paymentdataaccess.mapper;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.OrderId;
import com.learning.paymentdataaccess.entities.PaymentEntity;
import com.learning.paymentdomaincore.entities.Payment;
import com.learning.paymentdomaincore.valueobjects.PaymentId;

@Component
public class PaymentJPAMapper {

	public PaymentEntity paymentToPaymentEntity(Payment payment) {
		return PaymentEntity.builder().id(payment.getId().getValue()).customerId(payment.getCustomerId().getValue())
				.orderId(payment.getOrderId().getValue()).price(payment.getPrice().getAmount())
				.status(payment.getPaymentStatus()).createdAt(payment.getCreatedAt()).build();
	}

	public Payment paymentEntityToPayment(PaymentEntity paymentEntity) {
		return new Payment(new PaymentId(paymentEntity.getId()), new OrderId(paymentEntity.getOrderId()),
				new CustomerId(paymentEntity.getCustomerId()), new Money(paymentEntity.getPrice()));
	}
}
