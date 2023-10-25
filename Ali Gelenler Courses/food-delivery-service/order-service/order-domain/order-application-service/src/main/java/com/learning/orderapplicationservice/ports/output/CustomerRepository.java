package com.learning.orderapplicationservice.ports.output;

import java.util.Optional;
import java.util.UUID;

import com.learning.orderservice.core.entities.Customer;

public interface CustomerRepository {
    Optional<Customer> findCustomer(UUID customerId);

    Customer save(Customer customer);
}
