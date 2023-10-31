package com.learning.paymentapplicationservice.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.learning.commondomain.valueobjects.PaymentStatus;
import com.learning.outbox.OutboxStatus;
import com.learning.paymentapplicationservice.outbox.model.OrderOutboxMessage;

public interface OrderOutboxRepository {
	OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage);

	Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus status);

	Optional<OrderOutboxMessage> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String type, UUID sagaId,
			PaymentStatus paymentStatus, OutboxStatus outboxStatus);

	void deleteByTypeAndOutboxStatus(String type, OutboxStatus status);
}