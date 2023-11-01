package com.learning.customerapplicationservice.mapper;

import org.springframework.stereotype.Component;

import com.learning.customerapplicationservice.dto.CreateCustomerCommand;
import com.learning.customerapplicationservice.dto.CreateCustomerResponse;
import com.learning.customerdomaincore.entities.Customer;

@Component
public class CustomerDataMapper {

	public Customer createCustomerCommandToCustomer(CreateCustomerCommand createCustomerCommand) {
		return new Customer(null, createCustomerCommand.getUsername(),
				createCustomerCommand.getFirstName(), createCustomerCommand.getLastName());
	}

	public CreateCustomerResponse customerToCreateCustomerResponse(Customer customer, String message) {
		return new CreateCustomerResponse(customer.getId().getValue(), message);
	}
}