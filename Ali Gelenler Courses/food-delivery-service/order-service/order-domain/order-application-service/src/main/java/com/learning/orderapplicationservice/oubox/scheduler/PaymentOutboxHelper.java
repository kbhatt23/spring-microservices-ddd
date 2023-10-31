package com.learning.orderapplicationservice.oubox.scheduler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.commondomain.valueobjects.OrderStatus;
import com.learning.orderapplicationservice.oubox.model.OrderPaymentEventPayload;
import com.learning.orderapplicationservice.oubox.model.OrderPaymentOutboxMessage;
import com.learning.orderapplicationservice.ports.output.PaymentOutboxRepository;
import com.learning.orderservice.core.exceptions.OrderDomainException;
import com.learning.outbox.OutboxStatus;
import com.learning.saga.SagaStatus;
import com.learning.saga.constants.SagaConstants;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentOutboxHelper {

	private final PaymentOutboxRepository paymentOutboxRepository;

	private final ObjectMapper objectMapper;

	public PaymentOutboxHelper(PaymentOutboxRepository paymentOutboxRepository, ObjectMapper objectMapper) {
		this.paymentOutboxRepository = paymentOutboxRepository;
		this.objectMapper = objectMapper;
	}

	@Transactional(readOnly = true)
	public Optional<List<OrderPaymentOutboxMessage>> getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
			OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
		return paymentOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(SagaConstants.ORDER_SAGA_NAME,
				outboxStatus, sagaStatus);
	}

	@Transactional(readOnly = true)
	public Optional<OrderPaymentOutboxMessage> getPaymentOutboxMessageBySagaIdAndSagaStatus(UUID sagaId,
			SagaStatus... sagaStatus) {
		return paymentOutboxRepository.findByTypeAndSagaIdAndSagaStatus(SagaConstants.ORDER_SAGA_NAME, sagaId,
				sagaStatus);
	}

	@Transactional
	public void save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
		OrderPaymentOutboxMessage response = paymentOutboxRepository.save(orderPaymentOutboxMessage);
		if (response == null || response.getId() == null) {
			log.error("Could not save OrderPaymentOutboxMessage with outbox id: {}", orderPaymentOutboxMessage.getId());
			throw new OrderDomainException(
					"Could not save OrderPaymentOutboxMessage with outbox id: " + orderPaymentOutboxMessage.getId());
		}
		log.info("OrderPaymentOutboxMessage saved with outbox id: {}", orderPaymentOutboxMessage.getId());
	}

	@Transactional
	public void savePaymentOutboxMessage(OrderPaymentEventPayload paymentEventPayload, OrderStatus orderStatus,
			SagaStatus sagaStatus, OutboxStatus outboxStatus, UUID sagaId) {
		save(OrderPaymentOutboxMessage.builder().id(UUID.randomUUID()).sagaId(sagaId)
				.createdAt(paymentEventPayload.getCreatedAt()).type(SagaConstants.ORDER_SAGA_NAME)
				.payload(createPayload(paymentEventPayload)).orderStatus(orderStatus).sagaStatus(sagaStatus)
				.outboxStatus(outboxStatus).build());
	}

	@Transactional
	public void deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
			SagaStatus... sagaStatus) {
		paymentOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(SagaConstants.ORDER_SAGA_NAME, outboxStatus,
				sagaStatus);
	}

	private String createPayload(OrderPaymentEventPayload paymentEventPayload) {
		try {
			return objectMapper.writeValueAsString(paymentEventPayload);
		} catch (JsonProcessingException e) {
			log.error("Could not create OrderPaymentEventPayload object for order id: {}",
					paymentEventPayload.getOrderId(), e);
			throw new OrderDomainException("Could not create OrderPaymentEventPayload object for order id: "
					+ paymentEventPayload.getOrderId(), e);
		}
	}
}
