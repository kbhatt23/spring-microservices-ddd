package com.learning.ordermessaging.adapter.publisher;

import org.springframework.stereotype.Component;

import com.learning.kafkamodel.order.RestaurantApprovalRequestAvroModel;
import com.learning.kafkaproducer.service.KafkaEventHelper;
import com.learning.kafkaproducer.service.KafkaProducer;
import com.learning.orderapplicationservice.config.OrderServiceConfigData;
import com.learning.orderapplicationservice.ports.output.OrderPaidRestrauntRequestEventPublisher;
import com.learning.ordermessaging.helper.OrderMessagingMapper;
import com.learning.orderservice.core.events.OrderPaidEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderPaidEventPublisher implements OrderPaidRestrauntRequestEventPublisher {

	private final OrderMessagingMapper orderMessagingMapper;

	private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;

	private final OrderServiceConfigData orderServiceConfigData;

	private final KafkaEventHelper kafkaEventHelper;

	public OrderPaidEventPublisher(OrderMessagingMapper orderMessagingMapper,
			KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer, OrderServiceConfigData orderServiceConfigData,
			KafkaEventHelper kafkaEventHelper) {
		super();
		this.orderMessagingMapper = orderMessagingMapper;
		this.kafkaProducer = kafkaProducer;
		this.orderServiceConfigData = orderServiceConfigData;
		this.kafkaEventHelper = kafkaEventHelper;
	}

	@Override
	public void publish(OrderPaidEvent domainEvent) {

		String orderId = domainEvent.getOrder().getId().getValue().toString();
		try {
			String topicName = orderServiceConfigData.getRestaurantApprovalRequestTopicName();

			RestaurantApprovalRequestAvroModel orderApprovalEventToRestaurantApprovalRequestAvroModel = orderMessagingMapper
					.orderApprovalEventToRestaurantApprovalRequestAvroModel(domainEvent);

			kafkaProducer.publish(topicName, orderId, orderApprovalEventToRestaurantApprovalRequestAvroModel,
					kafkaEventHelper.callBack(topicName, orderApprovalEventToRestaurantApprovalRequestAvroModel, this.getClass().getSimpleName()));
			log.info("domainEvent: succesfully iniated OrderPaidEvent for order: {}", orderId);
		} catch (Exception e) {
			log.info("domainEvent: error while iniating OrderPaidEvent for order: {}", orderId);
		}

	}

}
