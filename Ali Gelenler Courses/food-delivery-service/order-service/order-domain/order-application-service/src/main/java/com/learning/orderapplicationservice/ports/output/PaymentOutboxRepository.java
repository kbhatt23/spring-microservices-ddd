package com.learning.orderapplicationservice.ports.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.learning.orderapplicationservice.oubox.model.OrderPaymentOutboxMessage;
import com.learning.outbox.OutboxStatus;
import com.learning.saga.SagaStatus;

public interface PaymentOutboxRepository {

	OrderPaymentOutboxMessage save(OrderPaymentOutboxMessage orderPaymentOutboxMessage);

	Optional<List<OrderPaymentOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String type,
			OutboxStatus outboxStatus, SagaStatus... sagaStatuses);

	Optional<OrderPaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type, UUID sagaId,
			SagaStatus... sagaStatuses);

	void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatuses);
}
