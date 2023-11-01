package com.learning.customerdomaincore.entities;

import java.util.UUID;

import com.learning.commondomain.entities.AggregateRoot;
import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.customerdomaincore.exceptions.CustomerDomainException;

public class Customer extends AggregateRoot<CustomerId> {

	private final String userName;

	private final String firstName;

	private final String lastName;

	public Customer(CustomerId id, String userName, String firstName, String lastName) {
		super(id);
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public void validateCustomer() {
		if (getId() != null) {
			throw new CustomerDomainException("Customer is not in correct state for initialization!");
		}
		if (this.userName == null || this.userName.isBlank()) {
			throw new CustomerDomainException("Customer userName can not be blank!");
		}
	}

	public void initializeCustomer() {
		super.setId(new CustomerId(UUID.randomUUID()));
	}

	public String getUserName() {
		return userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

}
