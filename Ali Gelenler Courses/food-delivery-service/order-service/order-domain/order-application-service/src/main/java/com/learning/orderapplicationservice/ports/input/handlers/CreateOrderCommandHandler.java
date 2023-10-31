package com.learning.orderapplicationservice.ports.input.handlers;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learning.orderapplicationservice.commands.CreateOrderCommand;
import com.learning.orderapplicationservice.mapper.OrderMapper;
import com.learning.orderapplicationservice.oubox.scheduler.PaymentOutboxHelper;
import com.learning.orderapplicationservice.ports.input.helper.OrderHelper;
import com.learning.orderapplicationservice.ports.input.helper.OrderSagaHelper;
import com.learning.orderapplicationservice.response.CreateOrderResponse;
import com.learning.orderservice.core.events.OrderCreatedEvent;
import com.learning.outbox.OutboxStatus;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreateOrderCommandHandler {

	private final OrderHelper orderHelper;

	private final OrderMapper orderMapper;

	private final PaymentOutboxHelper paymentOutboxHelper;

	private final OrderSagaHelper orderSagaHelper;

	public CreateOrderCommandHandler(OrderHelper orderHelper, OrderMapper orderMapper,
			PaymentOutboxHelper paymentOutboxHelper, OrderSagaHelper orderSagaHelper) {
		this.orderHelper = orderHelper;
		this.orderMapper = orderMapper;
		this.paymentOutboxHelper = paymentOutboxHelper;
		this.orderSagaHelper = orderSagaHelper;
	}

	@Transactional
	public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
		OrderCreatedEvent orderCreatedEvent = orderHelper.persistOrder(createOrderCommand);
		log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
		// TODO: send event to kafka but only after db saving
		// will be done in order-messaging-module

		// better to use strategy like second option
		// orderCreatedPaymentRequestEventPublisher.publish(persistOrder);

		 CreateOrderResponse createOrderResponse = orderMapper.createOrderResponse(orderCreatedEvent.getOrder(), "order created succesfully");
		 paymentOutboxHelper.savePaymentOutboxMessage(orderMapper.orderCreatedEventToOrderPaymentEventPayload(orderCreatedEvent)
				 		, orderCreatedEvent.getOrder().getOrderStatus(),
				 		orderSagaHelper.orderStatusToSagaStatus(orderCreatedEvent.getOrder().getOrderStatus()),
				 		OutboxStatus.STARTED,
				 		UUID.randomUUID());
		 
	     log.info("Returning CreateOrderResponse with order id: {}", orderCreatedEvent.getOrder().getId());
		 return createOrderResponse;
	}
}
