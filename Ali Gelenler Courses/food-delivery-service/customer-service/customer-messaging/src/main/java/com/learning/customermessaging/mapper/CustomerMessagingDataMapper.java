package com.learning.customermessaging.mapper;

import org.springframework.stereotype.Component;

import com.learning.customerdomaincore.events.CustomerCreatedEvent;
import com.learning.kafkamodel.order.CustomerAvroModel;

@Component
public class CustomerMessagingDataMapper {

	public CustomerAvroModel customerEventToCustomerAvroModel(CustomerCreatedEvent customerCreatedEvent) {
		return CustomerAvroModel.newBuilder().setId(customerCreatedEvent.getCustomer().getId().getValue().toString())
				.setUserName(customerCreatedEvent.getCustomer().getUserName())
				.setFirstName(customerCreatedEvent.getCustomer().getFirstName())
				.setLastName(customerCreatedEvent.getCustomer().getLastName()).build();
	}
}