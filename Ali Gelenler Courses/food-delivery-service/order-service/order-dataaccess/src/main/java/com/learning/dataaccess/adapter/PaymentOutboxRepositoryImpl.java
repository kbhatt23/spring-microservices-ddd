package com.learning.dataaccess.adapter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.learning.dataaccess.exceptions.PaymentOutboxNotFoundException;
import com.learning.dataaccess.helper.PaymentOutboxDataAccessMapper;
import com.learning.dataaccess.repositories.PaymentOutboxJpaRepository;
import com.learning.orderapplicationservice.oubox.model.OrderPaymentOutboxMessage;
import com.learning.orderapplicationservice.ports.output.PaymentOutboxRepository;
import com.learning.outbox.OutboxStatus;
import com.learning.saga.SagaStatus;

@Component
public class PaymentOutboxRepositoryImpl implements PaymentOutboxRepository {

	private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;

	private final PaymentOutboxDataAccessMapper paymentOutboxDataAccessMapper;

	public PaymentOutboxRepositoryImpl(PaymentOutboxJpaRepository paymentOutboxJpaRepository,
			PaymentOutboxDataAccessMapper paymentOutboxDataAccessMapper) {
		super();
		this.paymentOutboxJpaRepository = paymentOutboxJpaRepository;
		this.paymentOutboxDataAccessMapper = paymentOutboxDataAccessMapper;
	}

	@Override
	public OrderPaymentOutboxMessage save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
		return paymentOutboxDataAccessMapper.paymentOutboxEntityToOrderPaymentOutboxMessage(
				paymentOutboxJpaRepository.save(paymentOutboxDataAccessMapper
						.orderPaymentOutboxMessageToOutboxEntity(orderPaymentOutboxMessage)));
	}

	@Override
	public Optional<List<OrderPaymentOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String sagaType,
			OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
		return Optional.of(paymentOutboxJpaRepository
				.findByTypeAndOutboxStatusAndSagaStatusIn(sagaType, outboxStatus, Arrays.asList(sagaStatus))
				.orElseThrow(() -> new PaymentOutboxNotFoundException(
						"Payment outbox object " + "could not be found for saga type " + sagaType))
				.stream().map(paymentOutboxDataAccessMapper::paymentOutboxEntityToOrderPaymentOutboxMessage)
				.collect(Collectors.toList()));
	}

	@Override
	public Optional<OrderPaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type, UUID sagaId,
			SagaStatus... sagaStatus) {
		return paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(type, sagaId, Arrays.asList(sagaStatus))
				.map(paymentOutboxDataAccessMapper::paymentOutboxEntityToOrderPaymentOutboxMessage);
	}

	@Override
	public void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus,
			SagaStatus... sagaStatus) {
		paymentOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus,
				Arrays.asList(sagaStatus));
	}

}
