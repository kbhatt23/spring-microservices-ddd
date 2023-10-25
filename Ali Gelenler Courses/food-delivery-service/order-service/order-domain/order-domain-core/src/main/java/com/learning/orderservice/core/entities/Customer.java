package com.learning.orderservice.core.entities;

import com.learning.commondomain.entities.AggregateRoot;
import com.learning.commondomain.valueobjects.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

	private final String userName;

	public Customer(CustomerId id, String userName) {
		super(id);
		this.userName = userName;
	}

	public String getUserName() {
		return this.userName;
	}

}
