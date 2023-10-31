package com.learning.ordermessaging.adapter.publisher;

import java.util.function.BiConsumer;

import org.springframework.stereotype.Component;

import com.learning.kafkamodel.order.RestaurantApprovalRequestAvroModel;
import com.learning.kafkaproducer.service.KafkaEventHelper;
import com.learning.kafkaproducer.service.KafkaProducer;
import com.learning.orderapplicationservice.config.OrderServiceConfigData;
import com.learning.orderapplicationservice.oubox.model.OrderApprovalEventPayload;
import com.learning.orderapplicationservice.oubox.model.OrderApprovalOutboxMessage;
import com.learning.orderapplicationservice.ports.output.RestaurantApprovalRequestOutboxMessagePublisher;
import com.learning.ordermessaging.helper.OrderMessagingMapper;
import com.learning.outbox.OutboxStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderApprovalEventKafkaPublisher implements RestaurantApprovalRequestOutboxMessagePublisher {

	private final OrderMessagingMapper orderMessagingDataMapper;
	private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
	private final OrderServiceConfigData orderServiceConfigData;
	private final KafkaEventHelper kafkaMessageHelper;

	public OrderApprovalEventKafkaPublisher(OrderMessagingMapper orderMessagingDataMapper,
			KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer,
			OrderServiceConfigData orderServiceConfigData, KafkaEventHelper kafkaMessageHelper) {
		this.orderMessagingDataMapper = orderMessagingDataMapper;
		this.kafkaProducer = kafkaProducer;
		this.orderServiceConfigData = orderServiceConfigData;
		this.kafkaMessageHelper = kafkaMessageHelper;
	}

	@Override
	public void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
			BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback) {
		OrderApprovalEventPayload orderApprovalEventPayload = kafkaMessageHelper
				.getOrderEventPayload(orderApprovalOutboxMessage.getPayload(), OrderApprovalEventPayload.class);

		String sagaId = orderApprovalOutboxMessage.getSagaId().toString();

		log.info("Received OrderApprovalOutboxMessage for order id: {} and saga id: {}",
				orderApprovalEventPayload.getOrderId(), sagaId);

		try {
			RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = orderMessagingDataMapper
					.orderApprovalEventToRestaurantApprovalRequestAvroModel(sagaId, orderApprovalEventPayload);

			String restaurantApprovalRequestTopicName = orderServiceConfigData.getRestaurantApprovalRequestTopicName();
			
			kafkaProducer.publish(restaurantApprovalRequestTopicName, sagaId, restaurantApprovalRequestAvroModel,
					kafkaMessageHelper.callBack(restaurantApprovalRequestTopicName, restaurantApprovalRequestAvroModel,
							this.getClass().getSimpleName(), orderApprovalOutboxMessage, outboxCallback));

			log.info("OrderApprovalEventPayload sent to kafka for order id: {} and saga id: {}",
					restaurantApprovalRequestAvroModel.getOrderId(), sagaId);
		} catch (Exception e) {
			log.error("Error while sending OrderApprovalEventPayload to kafka for order id: {} and saga id: {},"
					+ " error: {}", orderApprovalEventPayload.getOrderId(), sagaId, e.getMessage());
		}

	}
}