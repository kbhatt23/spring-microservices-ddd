package com.learning.paymentapplicationservice.output;

import java.util.function.BiConsumer;

import com.learning.outbox.OutboxStatus;
import com.learning.paymentapplicationservice.outbox.model.OrderOutboxMessage;

public interface PaymentResponseMessagePublisher {

	void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}