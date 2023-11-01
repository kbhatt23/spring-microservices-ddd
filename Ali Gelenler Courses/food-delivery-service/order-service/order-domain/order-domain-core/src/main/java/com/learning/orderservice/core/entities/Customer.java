package com.learning.orderservice.core.entities;

import com.learning.commondomain.entities.AggregateRoot;
import com.learning.commondomain.valueobjects.CustomerId;

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

	public String getUserName() {
		return this.userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

}
