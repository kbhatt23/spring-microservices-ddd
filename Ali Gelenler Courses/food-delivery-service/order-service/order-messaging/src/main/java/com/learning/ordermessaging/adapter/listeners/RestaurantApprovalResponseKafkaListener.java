package com.learning.ordermessaging.adapter.listeners;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.learning.kafkaconsumer.KafkaConsumer;
import com.learning.kafkamodel.order.OrderApprovalStatus;
import com.learning.kafkamodel.order.RestaurantApprovalResponseAvroModel;
import com.learning.orderapplicationservice.external.RestrauntApprovalResponse;
import com.learning.orderapplicationservice.ports.input.RestrauntApprovalEventListener;
import com.learning.ordermessaging.helper.OrderMessagingMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

	private final RestrauntApprovalEventListener restrauntApprovalEventListener;

	private final OrderMessagingMapper orderMessagingMapper;

	public RestaurantApprovalResponseKafkaListener(RestrauntApprovalEventListener restrauntApprovalEventListener,
			OrderMessagingMapper orderMessagingMapper) {
		this.restrauntApprovalEventListener = restrauntApprovalEventListener;
		this.orderMessagingMapper = orderMessagingMapper;
	}

	@Override
	@KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}", topics = "${order-service.restaurant-approval-response-topic-name}")
	public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
			@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
			@Header(KafkaHeaders.OFFSET) List<Long> offsets) {
		log.info("{} number of restaurant approval responses received with keys {}, partitions {} and offsets {}",
				messages.size(), keys.toString(), partitions.toString(), offsets.toString());

		messages.forEach(message -> {
			RestrauntApprovalResponse restrauntApprovalResponse = orderMessagingMapper.approvalResponseAvroModelToApprovalResponse(message);
			if (OrderApprovalStatus.APPROVED == message.getOrderApprovalStatus()) {
				log.info("Processing approved order for order id: {}", message.getOrderId());
				restrauntApprovalEventListener
						.orderApproved(restrauntApprovalResponse);
			} else if (OrderApprovalStatus.REJECTED == message.getOrderApprovalStatus()) {
				log.info("Processing rejected order for order id: {}, with failure messages: {}", message.getOrderId(),
						message.getFailureMessages());
				restrauntApprovalEventListener
						.orderRejected(restrauntApprovalResponse);
			}
		});
	}

}
