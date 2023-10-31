package com.learning.ordermessaging.adapter.publisher;

import java.util.function.BiConsumer;

import org.springframework.stereotype.Component;

import com.learning.kafkamodel.order.PaymentRequestAvroModel;
import com.learning.kafkaproducer.service.KafkaEventHelper;
import com.learning.kafkaproducer.service.KafkaProducer;
import com.learning.orderapplicationservice.config.OrderServiceConfigData;
import com.learning.orderapplicationservice.oubox.model.OrderPaymentEventPayload;
import com.learning.orderapplicationservice.oubox.model.OrderPaymentOutboxMessage;
import com.learning.orderapplicationservice.ports.output.PaymentRequestOutboxMessagePublisher;
import com.learning.ordermessaging.helper.OrderMessagingMapper;
import com.learning.outbox.OutboxStatus;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderPaymentEventKafkaPublisher implements PaymentRequestOutboxMessagePublisher {

	private final OrderMessagingMapper orderMessagingMapper;

	private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

	private final KafkaEventHelper kafkaEventHelper;

	private final OrderServiceConfigData orderServiceConfigData;

	public OrderPaymentEventKafkaPublisher(OrderMessagingMapper orderMessagingMapper,
			KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer, KafkaEventHelper kafkaEventHelper,
			OrderServiceConfigData orderServiceConfigData) {
		this.orderMessagingMapper = orderMessagingMapper;
		this.kafkaProducer = kafkaProducer;
		this.kafkaEventHelper = kafkaEventHelper;
		this.orderServiceConfigData = orderServiceConfigData;
	}

	@Override
	public void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
			BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback) {
		OrderPaymentEventPayload orderPaymentEventPayload = kafkaEventHelper
				.getOrderEventPayload(orderPaymentOutboxMessage.getPayload(), OrderPaymentEventPayload.class);

		String sagaId = orderPaymentOutboxMessage.getSagaId().toString();

		log.info("Received OrderPaymentOutboxMessage for order id: {} and saga id: {}",
				orderPaymentEventPayload.getOrderId(), sagaId);
		try {
			PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingMapper
					.orderPaymentEventToPaymentRequestAvroModel(sagaId, orderPaymentEventPayload);

			String paymentRequestTopicName = orderServiceConfigData.getPaymentRequestTopicName();
			kafkaProducer.publish(paymentRequestTopicName, sagaId, paymentRequestAvroModel,
					kafkaEventHelper.callBack(paymentRequestTopicName, paymentRequestAvroModel,
							this.getClass().getSimpleName(), orderPaymentOutboxMessage, outboxCallback));
			log.info("OrderPaymentEventPayload sent to Kafka for order id: {} and saga id: {}",
					orderPaymentEventPayload.getOrderId(), sagaId);
		} catch (Exception e) {
			log.error(
					"Error while sending OrderPaymentEventPayload"
							+ " to kafka with order id: {} and saga id: {}, error: {}",
					orderPaymentEventPayload.getOrderId(), sagaId, e.getMessage());
		}
	}

}
