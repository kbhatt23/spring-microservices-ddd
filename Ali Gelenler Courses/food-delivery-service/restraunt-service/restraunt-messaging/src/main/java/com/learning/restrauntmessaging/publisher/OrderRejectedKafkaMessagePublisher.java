package com.learning.restrauntmessaging.publisher;

import org.springframework.stereotype.Component;

import com.learning.kafkamodel.order.RestaurantApprovalResponseAvroModel;
import com.learning.kafkaproducer.service.KafkaEventHelper;
import com.learning.kafkaproducer.service.KafkaProducer;
import com.learning.restrauntapplicationservice.config.RestrauntServiceConfigData;
import com.learning.restrauntapplicationservice.output.OrderRejectedMessagePublisher;
import com.learning.restrauntdomaincore.events.OrderRejectedEvent;
import com.learning.restrauntmessaging.mapper.RestaurantMessagingDataMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderRejectedKafkaMessagePublisher implements OrderRejectedMessagePublisher {

	private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;

	private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;

	private final RestrauntServiceConfigData restrauntServiceConfigData;

	private final KafkaEventHelper kafkaEventHelper;

	public OrderRejectedKafkaMessagePublisher(RestaurantMessagingDataMapper restaurantMessagingDataMapper,
			KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer,
			RestrauntServiceConfigData restrauntServiceConfigData, KafkaEventHelper kafkaEventHelper) {
		this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
		this.kafkaProducer = kafkaProducer;
		this.restrauntServiceConfigData = restrauntServiceConfigData;
		this.kafkaEventHelper = kafkaEventHelper;
	}

	@Override
	public void publish(OrderRejectedEvent domainEvent) {
		String orderId = domainEvent.getOrderApproval().getOrderId().getValue().toString();

		log.info("Received OrderRejectedEvent for order id: {}", orderId);
		try {
			RestaurantApprovalResponseAvroModel model = restaurantMessagingDataMapper
					.orderRejectedEventToRestaurantApprovalResponseAvroModel(domainEvent);

			String topicName = restrauntServiceConfigData.getRestaurantApprovalResponseTopicName();
			kafkaProducer.publish(topicName, orderId, model,
					kafkaEventHelper.callBack(topicName, model, this.getClass().getSimpleName()));
			log.info("RestaurantApprovalResponseAvroModel sent to kafka at: {}", System.nanoTime());
		} catch (Exception e) {
			log.error("Error while sending RestaurantApprovalResponseAvroModel message"
					+ " to kafka with order id: {}, error: {}", orderId, e.getMessage());
		}

	}

}
