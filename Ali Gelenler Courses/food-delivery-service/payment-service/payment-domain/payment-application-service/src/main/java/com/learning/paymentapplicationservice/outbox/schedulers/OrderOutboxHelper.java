package com.learning.paymentapplicationservice.outbox.schedulers;

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
import com.learning.commondomain.valueobjects.PaymentStatus;
import com.learning.outbox.OutboxStatus;
import com.learning.paymentapplicationservice.outbox.model.OrderEventPayload;
import com.learning.paymentapplicationservice.outbox.model.OrderOutboxMessage;
import com.learning.paymentapplicationservice.output.OrderOutboxRepository;
import com.learning.paymentdomaincore.exceptions.PaymentDomainException;
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
	public Optional<OrderOutboxMessage> getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(UUID sagaId,
			PaymentStatus paymentStatus) {
		return orderOutboxRepository.findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(SagaConstants.ORDER_SAGA_NAME,
				sagaId, paymentStatus, OutboxStatus.COMPLETED);
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
	public void saveOrderOutboxMessage(OrderEventPayload orderEventPayload, PaymentStatus paymentStatus,
			OutboxStatus outboxStatus, UUID sagaId) {
		save(OrderOutboxMessage.builder().id(UUID.randomUUID()).sagaId(sagaId)
				.createdAt(orderEventPayload.getCreatedAt())
				.processedAt(ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)))
				.type(SagaConstants.ORDER_SAGA_NAME).payload(createPayload(orderEventPayload))
				.paymentStatus(paymentStatus).outboxStatus(outboxStatus).build());
	}

	@Transactional
	public void updateOutboxMessage(OrderOutboxMessage orderOutboxMessage, OutboxStatus outboxStatus) {
		orderOutboxMessage.setOutboxStatus(outboxStatus);
		save(orderOutboxMessage);
		log.info("Order outbox table status is updated as: {}", outboxStatus.name());
	}

	private String createPayload(OrderEventPayload orderEventPayload) {
		try {
			return objectMapper.writeValueAsString(orderEventPayload);
		} catch (JsonProcessingException e) {
			log.error("Could not create OrderEventPayload json!", e);
			throw new PaymentDomainException("Could not create OrderEventPayload json!", e);
		}
	}

	private void save(OrderOutboxMessage orderOutboxMessage) {
		OrderOutboxMessage response = orderOutboxRepository.save(orderOutboxMessage);
		if (response == null) {
			log.error("Could not save OrderOutboxMessage!");
			throw new PaymentDomainException("Could not save OrderOutboxMessage!");
		}
		log.info("OrderOutboxMessage is saved with id: {}", orderOutboxMessage.getId());
	}
}
