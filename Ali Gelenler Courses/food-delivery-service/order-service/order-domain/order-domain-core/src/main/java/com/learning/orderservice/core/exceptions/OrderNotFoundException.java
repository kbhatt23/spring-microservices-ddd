package com.learning.orderservice.core.exceptions;

import com.learning.commondomain.exceptions.DomainException;

public class OrderNotFoundException extends DomainException {

	private static final long serialVersionUID = -5768527062529952958L;

	public OrderNotFoundException(String message) {
		super(message);
	}

	public OrderNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
