package com.learning.customerapplicationservice.ports.output;

import com.learning.customerdomaincore.entities.Customer;

public interface CustomerRepository {

	public Customer createCustomer(Customer customer);
}
