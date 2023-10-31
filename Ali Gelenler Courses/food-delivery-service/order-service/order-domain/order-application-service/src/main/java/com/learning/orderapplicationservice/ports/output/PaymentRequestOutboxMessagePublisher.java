package com.learning.orderapplicationservice.ports.output;

import java.util.function.BiConsumer;

import com.learning.orderapplicationservice.oubox.model.OrderPaymentOutboxMessage;
import com.learning.outbox.OutboxStatus;

public interface PaymentRequestOutboxMessagePublisher {

	void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
			BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback);
}
