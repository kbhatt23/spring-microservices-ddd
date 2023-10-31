package com.learning.restrauntapplicationservice.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.learning.outbox.OutboxStatus;
import com.learning.restrauntapplicationservice.outbox.model.OrderOutboxMessage;

public interface OrderOutboxRepository {

	OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage);

	Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

	Optional<OrderOutboxMessage> findByTypeAndSagaIdAndOutboxStatus(String type, UUID sagaId,
			OutboxStatus outboxStatus);

	void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

}
