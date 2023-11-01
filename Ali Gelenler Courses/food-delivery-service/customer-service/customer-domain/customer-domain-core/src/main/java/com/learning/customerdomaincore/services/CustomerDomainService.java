package com.learning.customerdomaincore.services;

import com.learning.customerdomaincore.entities.Customer;
import com.learning.customerdomaincore.events.CustomerCreatedEvent;

public interface CustomerDomainService {

	public CustomerCreatedEvent validateAndInitializeCustomer(Customer customer);
}
