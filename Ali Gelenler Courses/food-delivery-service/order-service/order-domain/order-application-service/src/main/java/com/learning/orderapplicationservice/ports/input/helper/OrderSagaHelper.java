package com.learning.orderapplicationservice.ports.input.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.OrderId;
import com.learning.commondomain.valueobjects.OrderStatus;
import com.learning.orderapplicationservice.ports.output.OrderRepository;
import com.learning.orderservice.core.entities.Order;
import com.learning.orderservice.core.exceptions.OrderNotFoundException;
import com.learning.saga.SagaStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderSagaHelper {

	private OrderRepository orderRepository;

	public OrderSagaHelper(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public Order findOrder(String orderId) {
		return orderRepository.findById(new OrderId(UUID.fromString(orderId))).orElseThrow(() -> {
			log.error("Order with id: {} could not be found!", orderId);
			throw new OrderNotFoundException("Order with id " + orderId + " could not be found!");
		});
	}

	public void saveOrder(Order order) {
		orderRepository.save(order);
	}

	public SagaStatus orderStatusToSagaStatus(OrderStatus orderStatus) {
		switch (orderStatus) {
		case PAID:
			return SagaStatus.PROCESSING;
		case APPROVED:
			return SagaStatus.SUCCEEDED;
		case CANCELLING:
			return SagaStatus.COMPENSATING;
		case CANCELLED:
			return SagaStatus.COMPENSATED;
		default:
			return SagaStatus.STARTED;
		}
	}

}
