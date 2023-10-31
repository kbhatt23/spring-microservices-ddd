package com.learning.orderapplicationservice.ports.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.learning.orderapplicationservice.oubox.model.OrderApprovalOutboxMessage;
import com.learning.outbox.OutboxStatus;
import com.learning.saga.SagaStatus;

public interface ApprovalOutboxRepository {

	OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage);

	Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String type,
			OutboxStatus outboxStatus, SagaStatus... sagaStatus);

	Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type, UUID sagaId,
			SagaStatus... sagaStatus);

	void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatus);
}
