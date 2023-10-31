package com.learning.restrauntdataaccess.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.learning.outbox.OutboxStatus;
import com.learning.restrauntapplicationservice.outbox.model.OrderOutboxMessage;
import com.learning.restrauntapplicationservice.output.OrderOutboxRepository;
import com.learning.restrauntdataaccess.exceptions.OrderOutboxNotFoundException;
import com.learning.restrauntdataaccess.mapper.OrderOutboxDataAccessMapper;
import com.learning.restrauntdataaccess.repositories.OrderOutboxJpaRepository;

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
	public Optional<OrderOutboxMessage> findByTypeAndSagaIdAndOutboxStatus(String type, UUID sagaId,
			OutboxStatus outboxStatus) {
		return orderOutboxJpaRepository.findByTypeAndSagaIdAndOutboxStatus(type, sagaId, outboxStatus)
				.map(orderOutboxDataAccessMapper::orderOutboxEntityToOrderOutboxMessage);
	}

	@Override
	public void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {
		orderOutboxJpaRepository.deleteByTypeAndOutboxStatus(type, outboxStatus);
	}
}
