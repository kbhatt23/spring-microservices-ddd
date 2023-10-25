package com.learning.ordermessaging.adapter.publisher;

import org.springframework.stereotype.Component;

import com.learning.kafkamodel.order.PaymentRequestAvroModel;
import com.learning.kafkaproducer.service.KafkaEventHelper;
import com.learning.kafkaproducer.service.KafkaProducer;
import com.learning.orderapplicationservice.config.OrderServiceConfigData;
import com.learning.orderapplicationservice.ports.output.OrderCreatedPaymentRequestEventPublisher;
import com.learning.ordermessaging.helper.OrderMessagingMapper;
import com.learning.orderservice.core.events.OrderCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreateOrderEventPublisher implements OrderCreatedPaymentRequestEventPublisher {

	private final OrderMessagingMapper orderMessagingMapper;

	private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

	private final OrderServiceConfigData orderServiceConfigData;

	private final KafkaEventHelper kafkaEventHelper;

	public CreateOrderEventPublisher(OrderMessagingMapper orderMessagingMapper,
			KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer, OrderServiceConfigData orderServiceConfigData,
			KafkaEventHelper kafkaEventHelper) {
		this.orderMessagingMapper = orderMessagingMapper;
		this.kafkaProducer = kafkaProducer;
		this.orderServiceConfigData = orderServiceConfigData;
		this.kafkaEventHelper = kafkaEventHelper;
	}

	@Override
	public void publish(OrderCreatedEvent domainEvent) {
		String orderId = domainEvent.getOrder().getId().getValue().toString();
		try {
			String topicName = orderServiceConfigData.getPaymentRequestTopicName();

			PaymentRequestAvroModel paymentModelFromEvent = orderMessagingMapper
					.createPaymentModelFromEvent(domainEvent);

			kafkaProducer.publish(topicName, orderId, paymentModelFromEvent,
					kafkaEventHelper.callBack(topicName, paymentModelFromEvent, this.getClass().getSimpleName()));
			log.info("domainEvent: succesfully iniated createOrderEvent for order: {}", orderId);
		} catch (Exception e) {
			log.info("domainEvent: error while iniating createOrderEvent for order: {}", orderId);
		}
	}

}
