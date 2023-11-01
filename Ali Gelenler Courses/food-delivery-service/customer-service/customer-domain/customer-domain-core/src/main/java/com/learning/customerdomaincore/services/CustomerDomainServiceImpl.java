package com.learning.customerdomaincore.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.learning.commondomain.constants.CommonDomainConstants;
import com.learning.customerdomaincore.entities.Customer;
import com.learning.customerdomaincore.events.CustomerCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerDomainServiceImpl implements CustomerDomainService {

	@Override
	public CustomerCreatedEvent validateAndInitializeCustomer(Customer customer) {
		customer.validateCustomer();
		customer.initializeCustomer();
		
		//Any more business logic
		
		log.info("Customer with id: {} is initiated", customer.getId().getValue());
		return new CustomerCreatedEvent(customer, ZonedDateTime.now(ZoneId.of(CommonDomainConstants.UTC)));
	}

}
