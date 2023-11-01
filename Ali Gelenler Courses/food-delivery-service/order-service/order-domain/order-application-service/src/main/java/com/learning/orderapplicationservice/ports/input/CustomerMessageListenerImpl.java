package com.learning.orderapplicationservice.ports.input;

import org.springframework.stereotype.Service;

import com.learning.orderapplicationservice.mapper.OrderMapper;
import com.learning.orderapplicationservice.oubox.model.CustomerModel;
import com.learning.orderapplicationservice.ports.output.CustomerRepository;
import com.learning.orderservice.core.entities.Customer;
import com.learning.orderservice.core.exceptions.OrderDomainException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerMessageListenerImpl implements CustomerMessageListener {

    private final CustomerRepository customerRepository;
    private final OrderMapper orderDataMapper;

    public CustomerMessageListenerImpl(CustomerRepository customerRepository, OrderMapper orderDataMapper) {
        this.customerRepository = customerRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Override
    public void customerCreated(CustomerModel customerModel) {
        Customer customer = customerRepository.save(orderDataMapper.customerModelToCustomer(customerModel));
        if (customer == null) {
            log.error("Customer could not be created in order database with id: {}", customerModel.getId());
            throw new OrderDomainException("Customer could not be created in order database with id " +
                    customerModel.getId());
        }
        log.info("Customer is created in order database with id: {}", customer.getId());
    }
}