package com.learning.restrauntmessaging.publisher;

import java.util.function.BiConsumer;

import org.springframework.stereotype.Component;

import com.learning.kafkamodel.order.RestaurantApprovalResponseAvroModel;
import com.learning.kafkaproducer.service.KafkaEventHelper;
import com.learning.kafkaproducer.service.KafkaProducer;
import com.learning.outbox.OutboxStatus;
import com.learning.restrauntapplicationservice.config.RestrauntServiceConfigData;
import com.learning.restrauntapplicationservice.outbox.model.OrderEventPayload;
import com.learning.restrauntapplicationservice.outbox.model.OrderOutboxMessage;
import com.learning.restrauntapplicationservice.output.RestrauntApprovalResponseMessagePublisher;
import com.learning.restrauntmessaging.mapper.RestaurantMessagingDataMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestaurantApprovalEventKafkaPublisher implements RestrauntApprovalResponseMessagePublisher {

	private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
	private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
	private final RestrauntServiceConfigData restaurantServiceConfigData;
	private final KafkaEventHelper kafkaMessageHelper;

	public RestaurantApprovalEventKafkaPublisher(RestaurantMessagingDataMapper restaurantMessagingDataMapper,
			KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer,
			RestrauntServiceConfigData restaurantServiceConfigData, KafkaEventHelper kafkaMessageHelper) {
		this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
		this.kafkaProducer = kafkaProducer;
		this.restaurantServiceConfigData = restaurantServiceConfigData;
		this.kafkaMessageHelper = kafkaMessageHelper;
	}

	@Override
	public void publish(OrderOutboxMessage orderOutboxMessage,
			BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {
		OrderEventPayload orderEventPayload = kafkaMessageHelper.getOrderEventPayload(orderOutboxMessage.getPayload(),
				OrderEventPayload.class);

		String sagaId = orderOutboxMessage.getSagaId().toString();

		log.info("Received OrderOutboxMessage for order id: {} and saga id: {}", orderEventPayload.getOrderId(),
				sagaId);
		try {
			RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel = restaurantMessagingDataMapper
					.orderEventPayloadToRestaurantApprovalResponseAvroModel(sagaId, orderEventPayload);

			String restaurantApprovalResponseTopicName = restaurantServiceConfigData
					.getRestaurantApprovalResponseTopicName();
			kafkaProducer.publish(restaurantApprovalResponseTopicName, sagaId, restaurantApprovalResponseAvroModel,
					kafkaMessageHelper.callBack(restaurantApprovalResponseTopicName,
							restaurantApprovalResponseAvroModel, this.getClass().getSimpleName(), orderOutboxMessage,
							outboxCallback));

			log.info("RestaurantApprovalResponseAvroModel sent to kafka for order id: {} and saga id: {}",
					restaurantApprovalResponseAvroModel.getOrderId(), sagaId);
		} catch (Exception e) {
			log.error(
					"Error while sending RestaurantApprovalResponseAvroModel message"
							+ " to kafka with order id: {} and saga id: {}, error: {}",
					orderEventPayload.getOrderId(), sagaId, e.getMessage());
		}
	}

}