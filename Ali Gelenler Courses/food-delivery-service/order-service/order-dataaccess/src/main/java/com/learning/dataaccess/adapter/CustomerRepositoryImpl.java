package com.learning.dataaccess.adapter;

import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.learning.dataaccess.entity.CustomerEntity;
import com.learning.dataaccess.helper.CustomerDataAccessMapper;
import com.learning.dataaccess.repositories.CustomerJPARepository;
import com.learning.orderapplicationservice.ports.output.CustomerRepository;
import com.learning.orderservice.core.entities.Customer;

@Service
public class CustomerRepositoryImpl implements CustomerRepository {

	private final CustomerDataAccessMapper customerDataAccessMapper;
	
	private final CustomerJPARepository customerJPARepository;
	
	public CustomerRepositoryImpl(CustomerDataAccessMapper customerDataAccessMapper,
			CustomerJPARepository customerJPARepository) {
		this.customerDataAccessMapper = customerDataAccessMapper;
		this.customerJPARepository = customerJPARepository;
	}

	@Override
	public Optional<Customer> findCustomer(UUID customerId) {
		return customerJPARepository.findById(customerId)
		                     .map(customerDataAccessMapper :: createCustomerDomainFromCustomerEntity);
	}

	@Transactional
	@Override
	public Customer save(Customer customer) {
		CustomerEntity savedCustomerEntity = customerJPARepository.save(customerDataAccessMapper.createCustomerEntityFromCustomerDomain(customer));
		
		return customerDataAccessMapper.createCustomerDomainFromCustomerEntity(savedCustomerEntity);
	}

}
