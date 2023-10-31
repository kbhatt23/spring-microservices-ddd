package com.learning.paymentdataaccess.outputadapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.PaymentStatus;
import com.learning.outbox.OutboxStatus;
import com.learning.paymentapplicationservice.outbox.model.OrderOutboxMessage;
import com.learning.paymentapplicationservice.output.OrderOutboxRepository;
import com.learning.paymentdataaccess.exceptions.OrderOutboxNotFoundException;
import com.learning.paymentdataaccess.mapper.OrderOutboxDataAccessMapper;
import com.learning.paymentdataaccess.repositories.OrderOutboxJpaRepository;

@Component
public class OrderOutboxRepositoryImpl implements OrderOutboxRepository {

	private final OrderOutboxJpaRepository orderOutboxJpaRepository;
	private final OrderOutboxDataAccessMapper orderOutboxDataAccessMapper;

	public OrderOutboxRepositoryImpl(OrderOutboxJpaRepository orderOutboxJpaRepository,
			OrderOutboxDataAccessMapper orderOutboxDataAccessMapper) {
		this.orderOutboxJpaRepository = orderOutboxJpaRepository;
		this.orderOutboxDataAccessMapper = orderOutboxDataAccessMapper;
	}

	@Override
	public OrderOutboxMessage save(OrderOutboxMessage orderPaymentOutboxMessage) {
		return orderOutboxDataAccessMapper.orderOutboxEntityToOrderOutboxMessage(orderOutboxJpaRepository
				.save(orderOutboxDataAccessMapper.orderOutboxMessageToOutboxEntity(orderPaymentOutboxMessage)));
	}

	@Override
	public Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String sagaType, OutboxStatus outboxStatus) {
		return Optional.of(orderOutboxJpaRepository.findByTypeAndOutboxStatus(sagaType, outboxStatus)
				.orElseThrow(() -> new OrderOutboxNotFoundException(
						"Approval outbox object " + "cannot be found for saga type " + sagaType))
				.stream().map(orderOutboxDataAccessMapper::orderOutboxEntityToOrderOutboxMessage)
				.collect(Collectors.toList()));
	}

	@Override
	public Optional<OrderOutboxMessage> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String sagaType, UUID sagaId,
			PaymentStatus paymentStatus, OutboxStatus outboxStatus) {
		return orderOutboxJpaRepository
				.findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(sagaType, sagaId, paymentStatus, outboxStatus)
				.map(orderOutboxDataAccessMapper::orderOutboxEntityToOrderOutboxMessage);
	}

	@Override
	public void deleteByTypeAndOutboxStatus(String sagaType, OutboxStatus outboxStatus) {
		orderOutboxJpaRepository.deleteByTypeAndOutboxStatus(sagaType, outboxStatus);
	}
}
