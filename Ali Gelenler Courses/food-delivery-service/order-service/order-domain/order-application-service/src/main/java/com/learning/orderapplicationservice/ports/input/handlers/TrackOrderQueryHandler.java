package com.learning.orderapplicationservice.ports.input.handlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learning.orderapplicationservice.mapper.OrderMapper;
import com.learning.orderapplicationservice.ports.output.OrderRepository;
import com.learning.orderapplicationservice.queries.TrackOrderQuery;
import com.learning.orderapplicationservice.response.TrackOrderResponse;
import com.learning.orderservice.core.exceptions.OrderNotFoundException;
import com.learning.orderservice.core.valueobjects.TrackingId;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TrackOrderQueryHandler {

	private final OrderMapper orderMapper;

	private final OrderRepository orderRepository;

	public TrackOrderQueryHandler(OrderMapper orderMapper, OrderRepository orderRepository) {
		this.orderMapper = orderMapper;
		this.orderRepository = orderRepository;
	}

	@Transactional(readOnly = true)
	public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
		return orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()))
				.map(orderMapper::createTrackOrderResponseFromOrder)
				.orElseThrow(() -> {
					log.warn("trackOrder: order not found with tracking id:{}", trackOrderQuery.getOrderTrackingId());
					throw new OrderNotFoundException(
							"order not found with tracking id:" + trackOrderQuery.getOrderTrackingId());
				});

	}

}
