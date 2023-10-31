package com.learning.dataaccess.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.dataaccess.entity.ApprovalOutboxEntity;
import com.learning.outbox.OutboxStatus;
import com.learning.saga.SagaStatus;

@Repository
public interface ApprovalOutboxJpaRepository extends JpaRepository<ApprovalOutboxEntity, UUID> {

	Optional<List<ApprovalOutboxEntity>> findByTypeAndOutboxStatusAndSagaStatusIn(String type,
			OutboxStatus outboxStatus, List<SagaStatus> sagaStatus);

	Optional<ApprovalOutboxEntity> findByTypeAndSagaIdAndSagaStatusIn(String type, UUID sagaId,
			List<SagaStatus> sagaStatus);

	void deleteByTypeAndOutboxStatusAndSagaStatusIn(String type, OutboxStatus outboxStatus,
			List<SagaStatus> sagaStatus);

}
