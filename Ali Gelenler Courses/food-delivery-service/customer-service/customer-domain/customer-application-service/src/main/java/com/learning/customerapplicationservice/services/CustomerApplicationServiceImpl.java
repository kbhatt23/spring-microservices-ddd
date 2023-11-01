package com.learning.customerapplicationservice.services;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.learning.customerapplicationservice.dto.CreateCustomerCommand;
import com.learning.customerapplicationservice.dto.CreateCustomerResponse;
import com.learning.customerapplicationservice.handlers.CustomerCreateCommandHandler;
import com.learning.customerapplicationservice.mapper.CustomerDataMapper;
import com.learning.customerapplicationservice.ports.input.CustomerApplicationService;
import com.learning.customerapplicationservice.ports.output.CustomerMessagePublisher;
import com.learning.customerdomaincore.events.CustomerCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Validated
public class CustomerApplicationServiceImpl implements CustomerApplicationService {

	private final CustomerDataMapper customerDataMapper;

	private final CustomerMessagePublisher customerMessagePublisher;

	private final CustomerCreateCommandHandler customerCreateCommandHandler;

	public CustomerApplicationServiceImpl(CustomerDataMapper customerDataMapper,
			CustomerMessagePublisher customerMessagePublisher,
			CustomerCreateCommandHandler customerCreateCommandHandler) {
		super();
		this.customerDataMapper = customerDataMapper;
		this.customerMessagePublisher = customerMessagePublisher;
		this.customerCreateCommandHandler = customerCreateCommandHandler;
	}

	@Override
	public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomerCommand) {

		CustomerCreatedEvent customerCreatedEvent = customerCreateCommandHandler.createCustomer(createCustomerCommand);

		customerMessagePublisher.publish(customerCreatedEvent);
		return customerDataMapper.customerToCreateCustomerResponse(customerCreatedEvent.getCustomer(),
				"Customer saved successfully!");
	}

}
