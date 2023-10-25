package com.learning.paymentdomaincore.exceptions;

import com.learning.commondomain.exceptions.DomainException;

public class PaymentDomainException extends DomainException {

	private static final long serialVersionUID = -2950326733567673401L;

	public PaymentDomainException(String message) {
		super(message);
	}

	public PaymentDomainException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
