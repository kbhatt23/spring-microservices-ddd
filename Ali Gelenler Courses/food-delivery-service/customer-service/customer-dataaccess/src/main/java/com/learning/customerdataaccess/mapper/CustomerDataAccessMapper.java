package com.learning.customerdataaccess.mapper;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.customerdataaccess.entities.CustomerEntity;
import com.learning.customerdomaincore.entities.Customer;

@Component
public class CustomerDataAccessMapper {

	public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
		return new Customer(new CustomerId(customerEntity.getId()), customerEntity.getUserName(),
				customerEntity.getFirstName(), customerEntity.getLastName());
	}

	public CustomerEntity customerToCustomerEntity(Customer customer) {
		return CustomerEntity.builder().id(customer.getId().getValue()).userName(customer.getUserName())
				.firstName(customer.getFirstName()).lastName(customer.getLastName()).build();
	}

}