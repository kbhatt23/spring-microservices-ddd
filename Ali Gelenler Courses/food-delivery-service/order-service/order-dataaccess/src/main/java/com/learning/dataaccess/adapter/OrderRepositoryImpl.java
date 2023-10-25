package com.learning.dataaccess.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.OrderId;
import com.learning.dataaccess.entity.OrderEntity;
import com.learning.dataaccess.helper.OrderDataAccessMapper;
import com.learning.dataaccess.repositories.OrderJPARepository;
import com.learning.orderapplicationservice.ports.output.OrderRepository;
import com.learning.orderservice.core.entities.Order;
import com.learning.orderservice.core.valueobjects.TrackingId;

@Component
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJPARepository orderJPARepository;

	private final OrderDataAccessMapper orderDataAccessMapper;

	public OrderRepositoryImpl(OrderJPARepository orderJPARepository, OrderDataAccessMapper orderDataAccessMapper) {
		super();
		this.orderJPARepository = orderJPARepository;
		this.orderDataAccessMapper = orderDataAccessMapper;
	}

	@Override
	public Order save(Order order) {
		OrderEntity savedOrderEntity = orderJPARepository
				.save(orderDataAccessMapper.createOrderEntityFromOrderDomain(order));

		return orderDataAccessMapper.createOrderFromOrderEntity(savedOrderEntity);
	}

	@Override
	public Optional<Order> findByTrackingId(TrackingId trackingId) {

		return orderJPARepository.findByTrackingId(trackingId.getValue())
				.map(orderDataAccessMapper::createOrderFromOrderEntity);

	}

	@Override
	public Optional<Order> findById(OrderId orderId) {

		return orderJPARepository.findById(orderId.getValue())
				.map(orderDataAccessMapper::createOrderFromOrderEntity);
	}

}
