package com.learning.orderapplicationservice.ports.input.handlers;

import org.springframework.stereotype.Component;

import com.learning.commondomain.events.DomainEvent;
import com.learning.orderapplicationservice.commands.CreateOrderCommand;
import com.learning.orderapplicationservice.mapper.OrderMapper;
import com.learning.orderapplicationservice.ports.input.helper.OrderHelper;
import com.learning.orderapplicationservice.ports.output.OrderCreatedPaymentRequestEventPublisher;
import com.learning.orderapplicationservice.response.CreateOrderResponse;
import com.learning.orderservice.core.events.OrderCreatedEvent;
import com.learning.orderservice.core.events.OrderEvent;

@Component
public class CreateOrderCommandHandler {

	private final OrderHelper orderHelper;

	private final OrderMapper orderMapper;

	private final OrderCreatedPaymentRequestEventPublisher orderCreatedPaymentRequestEventPublisher;

	public CreateOrderCommandHandler(OrderHelper orderHelper, OrderMapper orderMapper,
			OrderCreatedPaymentRequestEventPublisher orderCreatedPaymentRequestEventPublisher) {
		this.orderHelper = orderHelper;
		this.orderMapper = orderMapper;
		this.orderCreatedPaymentRequestEventPublisher = orderCreatedPaymentRequestEventPublisher;
	}

	public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
		OrderCreatedEvent persistOrder = orderHelper.persistOrder(createOrderCommand,orderCreatedPaymentRequestEventPublisher);

		// TODO: send event to kafka but only after db saving
		//will be done in order-messaging-module
		
		//better to use strategy like second option
		//orderCreatedPaymentRequestEventPublisher.publish(persistOrder);
		
		fire(persistOrder);
		return orderMapper.createOrderResponse(persistOrder.getOrder(), "order created succesfully");
	}
	
	private void fire(OrderEvent orderEvent) {
		orderEvent.fire();
	}

}
