package com.learning.orderapplicationservice.ports.output;

import java.util.function.BiConsumer;

import com.learning.orderapplicationservice.oubox.model.OrderApprovalOutboxMessage;
import com.learning.outbox.OutboxStatus;

public interface RestaurantApprovalRequestOutboxMessagePublisher {

	void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
			BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback);
}
