package com.learning.paymentdomaincore.exceptions;

import com.learning.commondomain.exceptions.DomainException;

public class PaymentNotFoundException extends DomainException {

	private static final long serialVersionUID = -3747948747103248429L;

	public PaymentNotFoundException(String message) {
		super(message);
	}

	public PaymentNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
