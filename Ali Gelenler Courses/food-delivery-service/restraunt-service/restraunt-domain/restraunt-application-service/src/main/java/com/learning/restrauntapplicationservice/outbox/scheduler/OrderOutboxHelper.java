package com.learning.restrauntapplicationservice.outbox.scheduler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.commondomain.constants.CommonDomainConstants;
import com.learning.commondomain.valueobjects.OrderApprovalStatus;
import com.learning.outbox.OutboxStatus;
import com.learning.restrauntapplicationservice.outbox.model.OrderEventPayload;
import com.learning.restrauntapplicationservice.outbox.model.OrderOutboxMessage;
import com.learning.restrauntapplicationservice.output.OrderOutboxRepository;
import com.learning.restrauntdomaincore.exceptions.RestrauntDomainException;
import com.learning.saga.constants.SagaConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderOutboxHelper {

	private final OrderOutboxRepository orderOutboxRepository;
	private final ObjectMapper objectMapper;

	public OrderOutboxHelper(OrderOutboxRepository orderOutboxRepository, ObjectMapper objectMapper) {
		this.orderOutboxRepository = orderOutboxRepository;
		this.objectMapper = objectMapper;
	}

	@Transactional(readOnly = true)
	public Optional<OrderOutboxMessage> getCompletedOrderOutboxMessageBySagaIdAndOutboxStatus(UUID sagaId,
			OutboxStatus outboxStatus) {
		return orderOutboxRepository.findByTypeAndSagaIdAndOutboxStatus(SagaConstants.ORDER_SAGA_NAME, sagaId,
				outboxStatus);
	}

	@Transactional(readOnly = true)
	public Optional<List<OrderOutboxMessage>> getOrderOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
		return orderOutboxRepository.findByTypeAndOutboxStatus(SagaConstants.ORDER_SAGA_NAME, outboxStatus);
	}

	@Transactional
	public void deleteOrderOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
		orderOutboxRepository.deleteByTypeAndOutboxStatus(SagaConstants.ORDER_SAGA_NAME, outboxStatus);
	}

	@Transactional
	public void saveOrderOutboxMessage(OrderEventPayload orderEventPayload, OrderApprovalStatus approvalStatus,
			OutboxStatus outboxStatus, UUID sagaId) {
		save(OrderOutboxMessage.builder().id(UUID.randomUUID()).sagaId(sagaId)
				.createdAt(orderEventPayload.getCreatedAt())
				.processedAt(ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)))
				.type(SagaConstants.ORDER_SAGA_NAME).payload(createPayload(orderEventPayload))
				.approvalStatus(approvalStatus).outboxStatus(outboxStatus).build());
	}

	@Transactional
	public void updateOutboxStatus(OrderOutboxMessage orderPaymentOutboxMessage, OutboxStatus outboxStatus) {
		orderPaymentOutboxMessage.setOutboxStatus(outboxStatus);
		save(orderPaymentOutboxMessage);
		log.info("Order outbox table status is updated as: {}", outboxStatus.name());
	}

	private void save(OrderOutboxMessage orderPaymentOutboxMessage) {
		OrderOutboxMessage response = orderOutboxRepository.save(orderPaymentOutboxMessage);
		if (response == null) {
			throw new RestrauntDomainException("Could not save OrderOutboxMessage!");
		}
		log.info("OrderOutboxMessage saved with id: {}", orderPaymentOutboxMessage.getId());
	}

	private String createPayload(OrderEventPayload orderEventPayload) {
		try {
			return objectMapper.writeValueAsString(orderEventPayload);
		} catch (JsonProcessingException e) {
			log.error("Could not create OrderEventPayload json!", e);
			throw new RestrauntDomainException("Could not create OrderEventPayload json!", e);
		}
	}

}
