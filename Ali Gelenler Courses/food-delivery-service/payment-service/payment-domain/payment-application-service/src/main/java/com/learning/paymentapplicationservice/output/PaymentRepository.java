package com.learning.paymentapplicationservice.output;

import java.util.Optional;

import com.learning.commondomain.valueobjects.OrderId;
import com.learning.paymentdomaincore.entities.Payment;

public interface PaymentRepository {

	Payment save(Payment payment);

	Optional<Payment> findByOrderId(OrderId orderId);
}
