package com.learning.customerdomaincore.exceptions;

import com.learning.commondomain.exceptions.DomainException;

public class CustomerDomainException extends DomainException {

	private static final long serialVersionUID = -7989483420870816747L;

	public CustomerDomainException(String message) {
		super(message);
	}
}
