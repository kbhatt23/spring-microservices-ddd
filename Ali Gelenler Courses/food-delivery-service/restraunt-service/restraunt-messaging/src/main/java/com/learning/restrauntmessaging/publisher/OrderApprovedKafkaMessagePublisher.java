package com.learning.restrauntmessaging.publisher;

import org.springframework.stereotype.Component;

import com.learning.kafkamodel.order.RestaurantApprovalResponseAvroModel;
import com.learning.kafkaproducer.service.KafkaEventHelper;
import com.learning.kafkaproducer.service.KafkaProducer;
import com.learning.restrauntapplicationservice.config.RestrauntServiceConfigData;
import com.learning.restrauntapplicationservice.output.OrderApprovedMessagePublisher;
import com.learning.restrauntdomaincore.events.OrderApprovedEvent;
import com.learning.restrauntmessaging.mapper.RestaurantMessagingDataMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderApprovedKafkaMessagePublisher implements OrderApprovedMessagePublisher {

	private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;

	private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;

	private final RestrauntServiceConfigData restrauntServiceConfigData;

	private final KafkaEventHelper kafkaEventHelper;

	public OrderApprovedKafkaMessagePublisher(RestaurantMessagingDataMapper restaurantMessagingDataMapper,
			KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer,
			RestrauntServiceConfigData restrauntServiceConfigData, KafkaEventHelper kafkaEventHelper) {
		this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
		this.kafkaProducer = kafkaProducer;
		this.restrauntServiceConfigData = restrauntServiceConfigData;
		this.kafkaEventHelper = kafkaEventHelper;
	}

	@Override
	public void publish(OrderApprovedEvent domainEvent) {
		String orderId = domainEvent.getOrderApproval().getOrderId().getValue().toString();

		log.info("Received OrderApprovedEvent for order id: {}", orderId);
		try {
			RestaurantApprovalResponseAvroModel model = restaurantMessagingDataMapper
					.orderApprovedEventToRestaurantApprovalResponseAvroModel(domainEvent);

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
