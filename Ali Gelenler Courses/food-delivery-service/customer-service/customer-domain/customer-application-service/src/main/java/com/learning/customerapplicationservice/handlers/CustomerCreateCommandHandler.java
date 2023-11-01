package com.learning.customerapplicationservice.handlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learning.customerapplicationservice.dto.CreateCustomerCommand;
import com.learning.customerapplicationservice.mapper.CustomerDataMapper;
import com.learning.customerapplicationservice.ports.output.CustomerRepository;
import com.learning.customerdomaincore.entities.Customer;
import com.learning.customerdomaincore.events.CustomerCreatedEvent;
import com.learning.customerdomaincore.exceptions.CustomerDomainException;
import com.learning.customerdomaincore.services.CustomerDomainService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomerCreateCommandHandler {

	private final CustomerRepository customerRepository;

	private final CustomerDataMapper customerDataMapper;

	private final CustomerDomainService customerDomainService;

	public CustomerCreateCommandHandler(CustomerRepository customerRepository, CustomerDataMapper customerDataMapper,
			CustomerDomainService customerDomainService) {
		this.customerRepository = customerRepository;
		this.customerDataMapper = customerDataMapper;
		this.customerDomainService = customerDomainService;
	}

	@Transactional
	public CustomerCreatedEvent createCustomer(CreateCustomerCommand createCustomerCommand) {
		Customer customer = customerDataMapper.createCustomerCommandToCustomer(createCustomerCommand);

		CustomerCreatedEvent customerCreatedEvent = customerDomainService.validateAndInitializeCustomer(customer);
		Customer createdCustomer = customerRepository.createCustomer(customer);
		if (createdCustomer == null || createdCustomer.getId() == null) {
			log.error("Could not save customer with userName: {}", createCustomerCommand.getUsername());
			throw new CustomerDomainException(
					"Could not save customer with userName " + createCustomerCommand.getUsername());
		}

		log.info("Returning CustomerCreatedEvent for customer userName: {}", createCustomerCommand.getUsername());
		return customerCreatedEvent;
	}
}
