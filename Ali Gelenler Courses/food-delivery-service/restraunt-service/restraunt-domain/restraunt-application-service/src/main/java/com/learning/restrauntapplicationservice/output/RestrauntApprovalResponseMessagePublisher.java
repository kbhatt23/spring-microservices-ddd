package com.learning.restrauntapplicationservice.output;

import java.util.function.BiConsumer;

import com.learning.outbox.OutboxStatus;
import com.learning.restrauntapplicationservice.outbox.model.OrderOutboxMessage;

public interface RestrauntApprovalResponseMessagePublisher {

	void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}