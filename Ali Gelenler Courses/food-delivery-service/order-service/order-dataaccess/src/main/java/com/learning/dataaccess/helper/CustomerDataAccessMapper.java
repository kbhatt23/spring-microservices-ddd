package com.learning.dataaccess.helper;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.dataaccess.entity.CustomerEntity;
import com.learning.orderservice.core.entities.Customer;

@Component
public class CustomerDataAccessMapper {

	public CustomerEntity createCustomerEntityFromCustomerDomain(Customer customer) {
		return new CustomerEntity(customer.getId().getValue(),
				customer.getUserName(), customer.getFirstName(), customer.getLastName()
				);
	}
	
	public Customer createCustomerDomainFromCustomerEntity(CustomerEntity customerEntity) {
		return new Customer(new CustomerId(customerEntity.getId()), 
				 customerEntity.getUserName(), customerEntity.getFirstName(), customerEntity.getLastName()
				);
	}
}
