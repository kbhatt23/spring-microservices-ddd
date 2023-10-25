package com.learning.ordermessaging.adapter.publisher;

import org.springframework.stereotype.Component;

import com.learning.kafkamodel.order.PaymentRequestAvroModel;
import com.learning.kafkaproducer.service.KafkaEventHelper;
import com.learning.kafkaproducer.service.KafkaProducer;
import com.learning.orderapplicationservice.config.OrderServiceConfigData;
import com.learning.orderapplicationservice.ports.output.OrderCancelledPaymentRequestEventPublisher;
import com.learning.ordermessaging.helper.OrderMessagingMapper;
import com.learning.orderservice.core.events.OrderCancelledEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CancelOrderEventPublisher implements OrderCancelledPaymentRequestEventPublisher {

	private final OrderMessagingMapper orderMessagingMapper;

	private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

	private final OrderServiceConfigData orderServiceConfigData;

	private final KafkaEventHelper kafkaEventHelper;

	public CancelOrderEventPublisher(OrderMessagingMapper orderMessagingMapper,
			KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer, OrderServiceConfigData orderServiceConfigData,
			final KafkaEventHelper kafkaEventHelper) {
		this.orderMessagingMapper = orderMessagingMapper;
		this.kafkaProducer = kafkaProducer;
		this.orderServiceConfigData = orderServiceConfigData;
		this.kafkaEventHelper = kafkaEventHelper;
	}

	@Override
	public void publish(OrderCancelledEvent domainEvent) {
		String orderId = domainEvent.getOrder().getId().getValue().toString();
		try {
			String topicName = orderServiceConfigData.getPaymentRequestTopicName();

			PaymentRequestAvroModel paymentModelFromEvent = orderMessagingMapper
					.cancelPaymentModelFromEvent(domainEvent);

			kafkaProducer.publish(topicName, orderId, paymentModelFromEvent,
					kafkaEventHelper.callBack(topicName, paymentModelFromEvent, this.getClass().getSimpleName()));
			log.info("publish: succesfully iniated cancelOrderEvent for order: {}", orderId);
		} catch (Exception e) {
			log.info("publish: error while iniating cancelOrderEvent for order: {}", orderId);
		}
	}
}
