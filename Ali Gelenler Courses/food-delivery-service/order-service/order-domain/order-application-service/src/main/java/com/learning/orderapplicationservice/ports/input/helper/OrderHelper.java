package com.learning.orderapplicationservice.ports.input.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.orderapplicationservice.commands.CreateOrderCommand;
import com.learning.orderapplicationservice.mapper.OrderMapper;
import com.learning.orderapplicationservice.ports.output.CustomerRepository;
import com.learning.orderapplicationservice.ports.output.OrderRepository;
import com.learning.orderapplicationservice.ports.output.RestrauntRepository;
import com.learning.orderservice.core.entities.Order;
import com.learning.orderservice.core.entities.Restraunt;
import com.learning.orderservice.core.events.OrderCreatedEvent;
import com.learning.orderservice.core.exceptions.OrderDomainException;
import com.learning.orderservice.core.services.OrderDomainService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderHelper {

	private final OrderDomainService orderDomainService;

	private final OrderRepository orderRepository;

	private final RestrauntRepository restrauntRepository;

	private final CustomerRepository customerRepository;

	private final OrderMapper orderMapper;

	public OrderHelper(OrderDomainService orderDomainService, OrderRepository orderRepository,
			RestrauntRepository restrauntRepository, CustomerRepository customerRepository, OrderMapper orderMapper) {
		this.orderDomainService = orderDomainService;
		this.orderRepository = orderRepository;
		this.restrauntRepository = restrauntRepository;
		this.customerRepository = customerRepository;
		this.orderMapper = orderMapper;
	}
	
	@Transactional
	public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
		checkCustomer(createOrderCommand.getCustomerId());
		Restraunt restraunt = fetchValidRestraunt(createOrderCommand);
		Order order = orderMapper.createOrderFromOrderCommand(createOrderCommand);

		OrderCreatedEvent validateAndInitializeOrder = orderDomainService.validateAndInitializeOrder(order, restraunt);

		saveOrder(order);
		log.info("persistOrder: order created with id: "+validateAndInitializeOrder.getOrder().getId().getValue());
		return validateAndInitializeOrder;
	}
	
	private Order saveOrder(Order order) {
		Order savedOrder = orderRepository.save(order);
		if (savedOrder == null || savedOrder.getId() == null) {
			log.error("saveOrder: Could not save order!");
			throw new OrderDomainException("Could not save order!");
		}
		log.info("saveOrder: Order is saved with id: {}", savedOrder.getId().getValue());
		return savedOrder;
	}

	private Restraunt fetchValidRestraunt(CreateOrderCommand createOrderCommand) {
		Restraunt restraunt = orderMapper.getRestrauntFromCreateOrderCommand(createOrderCommand);

		return restrauntRepository.findRestaurantInformation(restraunt).orElseThrow(() -> {
			log.error("Could not find restaurant with restaurant id: {}", createOrderCommand.getRestrauntId());
			throw new OrderDomainException(
					"Could not find restaurant with restaurant id: " + createOrderCommand.getRestrauntId());
		});
	}

	private void checkCustomer(UUID customerId) {
		customerRepository.findCustomer(customerId).orElseThrow(() -> {
			log.error("checkCustomer: Could not find customer with customer id: {}", customerId);
			throw new OrderDomainException("Could not find customer with customer id: " + customerId);
		});
	}

}
