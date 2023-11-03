package com.learning.restrauntmessaging.listener;

import java.sql.SQLException;
import java.util.List;

import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.learning.kafkaconsumer.KafkaConsumer;
import com.learning.kafkamodel.order.RestaurantApprovalRequestAvroModel;
import com.learning.restrauntapplicationservice.exception.RestrauntApplicationServiceException;
import com.learning.restrauntapplicationservice.input.RestrauntApprovalRequestMessageListener;
import com.learning.restrauntdomaincore.exceptions.RestrauntNotFoundException;
import com.learning.restrauntmessaging.mapper.RestaurantMessagingDataMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestrauntApprovalRequestKafkaListener implements KafkaConsumer<RestaurantApprovalRequestAvroModel> {

	private final RestrauntApprovalRequestMessageListener restaurantApprovalRequestMessageListener;
	private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;

	public RestrauntApprovalRequestKafkaListener(
			RestrauntApprovalRequestMessageListener restaurantApprovalRequestMessageListener,
			RestaurantMessagingDataMapper restaurantMessagingDataMapper) {
		this.restaurantApprovalRequestMessageListener = restaurantApprovalRequestMessageListener;
		this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
	}

	@Override
	@KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}", topics = "${restaurant-service.restaurant-approval-request-topic-name}")
	public void receive(@Payload List<RestaurantApprovalRequestAvroModel> messages,
			@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
			@Header(KafkaHeaders.OFFSET) List<Long> offsets) {
		log.info(
				"{} number of orders approval requests received with keys {}, partitions {} and offsets {}"
						+ ", sending for restaurant approval",
				messages.size(), keys.toString(), partitions.toString(), offsets.toString());

		messages.forEach(restaurantApprovalRequestAvroModel -> {
			try {
				log.info("Processing order approval for order id: {}", restaurantApprovalRequestAvroModel.getOrderId());
				restaurantApprovalRequestMessageListener.approveOrder(restaurantMessagingDataMapper
						.restaurantApprovalRequestAvroModelToRestaurantApproval(restaurantApprovalRequestAvroModel));
			} catch (DataAccessException e) {
				SQLException sqlException = (SQLException) e.getRootCause();
				if (sqlException != null && sqlException.getSQLState() != null
						&& PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
					// NO-OP for unique constraint exception
					log.error(
							"Caught unique constraint exception with sql state: {} "
									+ "in RestaurantApprovalRequestKafkaListener for order id: {}",
							sqlException.getSQLState(), restaurantApprovalRequestAvroModel.getOrderId());
				} else {
					throw new RestrauntApplicationServiceException("Throwing DataAccessException in"
							+ " RestaurantApprovalRequestKafkaListener: " + e.getMessage(), e);
				}
			} catch (RestrauntNotFoundException e) {
				// NO-OP for RestaurantNotFoundException
				log.error("No restaurant found for restaurant id: {}, and order id: {}",
						restaurantApprovalRequestAvroModel.getRestaurantId(),
						restaurantApprovalRequestAvroModel.getOrderId());
			}
		});
	}

}