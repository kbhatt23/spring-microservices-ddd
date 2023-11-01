package com.learning.customerdomaincore.events;

import java.time.ZonedDateTime;

import com.learning.commondomain.events.DomainEvent;
import com.learning.customerdomaincore.entities.Customer;

public class CustomerCreatedEvent implements DomainEvent<Customer> {

	private final Customer customer;
	
	private final ZonedDateTime createdAt;

	public CustomerCreatedEvent(Customer customer, ZonedDateTime createdAt) {
		this.customer = customer;
		this.createdAt = createdAt;
	}

	public Customer getCustomer() {
		return customer;
	}

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}
	
}
