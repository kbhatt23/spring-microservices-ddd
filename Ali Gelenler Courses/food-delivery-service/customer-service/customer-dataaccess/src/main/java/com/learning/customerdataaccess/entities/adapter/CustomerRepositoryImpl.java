package com.learning.customerdataaccess.entities.adapter;

import org.springframework.stereotype.Component;

import com.learning.customerapplicationservice.ports.output.CustomerRepository;
import com.learning.customerdataaccess.entities.repository.CustomerJPARepository;
import com.learning.customerdataaccess.mapper.CustomerDataAccessMapper;
import com.learning.customerdomaincore.entities.Customer;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

	private final CustomerDataAccessMapper mapper;

	private final CustomerJPARepository repository;

	public CustomerRepositoryImpl(CustomerDataAccessMapper mapper, CustomerJPARepository repository) {
		this.mapper = mapper;
		this.repository = repository;
	}

	@Override
	public Customer createCustomer(Customer customer) {
		return mapper.customerEntityToCustomer(repository.save(mapper.customerToCustomerEntity(customer)));

	}

}
