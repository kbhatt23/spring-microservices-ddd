package com.learning.orderservice.core.exceptions;

import com.learning.commondomain.exceptions.DomainException;

public class OrderDomainException extends DomainException {

	private static final long serialVersionUID = -5768527062529952958L;

	public OrderDomainException(String message) {
		super(message);
	}

	public OrderDomainException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
