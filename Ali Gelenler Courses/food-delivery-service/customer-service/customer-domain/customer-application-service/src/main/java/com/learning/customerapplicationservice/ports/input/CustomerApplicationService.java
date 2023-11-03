package com.learning.customerapplicationservice.ports.input;

import jakarta.validation.Valid;

import com.learning.customerapplicationservice.dto.CreateCustomerCommand;
import com.learning.customerapplicationservice.dto.CreateCustomerResponse;

public interface CustomerApplicationService {

	public CreateCustomerResponse createCustomer(@Valid CreateCustomerCommand createCustomerCommand);
}
