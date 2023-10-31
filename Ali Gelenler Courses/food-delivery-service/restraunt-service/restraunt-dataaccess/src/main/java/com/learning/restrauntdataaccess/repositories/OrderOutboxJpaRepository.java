package com.learning.restrauntdataaccess.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.outbox.OutboxStatus;
import com.learning.restrauntdataaccess.entities.OrderOutboxEntity;

@Repository
public interface OrderOutboxJpaRepository extends JpaRepository<OrderOutboxEntity, UUID> {

	Optional<List<OrderOutboxEntity>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

	Optional<OrderOutboxEntity> findByTypeAndSagaIdAndOutboxStatus(String type, UUID sagaId, OutboxStatus outboxStatus);

	void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

}
