package com.learning.customermessaging.publisher;

import org.springframework.stereotype.Component;

import com.learning.customerapplicationservice.config.CustomerServiceConfigData;
import com.learning.customerapplicationservice.ports.output.CustomerMessagePublisher;
import com.learning.customerdomaincore.events.CustomerCreatedEvent;
import com.learning.customermessaging.mapper.CustomerMessagingDataMapper;
import com.learning.kafkamodel.order.CustomerAvroModel;
import com.learning.kafkaproducer.service.KafkaEventHelper;
import com.learning.kafkaproducer.service.KafkaProducer;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomerCreatedEventKafkaPublisher implements CustomerMessagePublisher {

	private final CustomerMessagingDataMapper mappper;

	private final KafkaProducer<String, CustomerAvroModel> kafkaProducer;

	private final KafkaEventHelper kafkaEventHelper;

	private final CustomerServiceConfigData customerServiceConfigData;

	public CustomerCreatedEventKafkaPublisher(CustomerMessagingDataMapper mappper,
			KafkaProducer<String, CustomerAvroModel> kafkaProducer, KafkaEventHelper kafkaEventHelper,
			CustomerServiceConfigData customerServiceConfigData) {
		this.mappper = mappper;
		this.kafkaProducer = kafkaProducer;
		this.kafkaEventHelper = kafkaEventHelper;
		this.customerServiceConfigData = customerServiceConfigData;
	}

	@Override
	public void publish(CustomerCreatedEvent customerCreatedEvent) {
		log.info("Received CustomerCreatedEvent for customer id: {}",
				customerCreatedEvent.getCustomer().getId().getValue());

		try {
			CustomerAvroModel customerAvroModel = mappper.customerEventToCustomerAvroModel(customerCreatedEvent);
			String customerTopicName = customerServiceConfigData.getCustomerTopicName();

			kafkaProducer.publish(customerTopicName, customerAvroModel.getId(), customerAvroModel, kafkaEventHelper
					.callBack(customerTopicName, customerAvroModel, this.getClass().getSimpleName(), null, null));

			log.info("CustomerCreatedEvent sent to kafka for customer id: {}", customerAvroModel.getId());

		} catch (Exception e) {
			log.error("Error while sending CustomerCreatedEvent to kafka for customer id: {}," + " error: {}",
					customerCreatedEvent.getCustomer().getId().getValue(), e.getMessage());
		}

	}

}
