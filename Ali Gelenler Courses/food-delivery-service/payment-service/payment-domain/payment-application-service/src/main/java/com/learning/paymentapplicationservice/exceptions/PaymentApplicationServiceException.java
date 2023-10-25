package com.learning.paymentapplicationservice.exceptions;

import com.learning.commondomain.exceptions.DomainException;

public class PaymentApplicationServiceException extends DomainException {

	private static final long serialVersionUID = -8255229038037999063L;

	public PaymentApplicationServiceException(String message) {
		super(message);
	}

	public PaymentApplicationServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
