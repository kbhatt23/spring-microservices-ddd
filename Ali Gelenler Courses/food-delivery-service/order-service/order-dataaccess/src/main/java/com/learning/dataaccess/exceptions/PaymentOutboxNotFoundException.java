package com.learning.dataaccess.exceptions;

public class PaymentOutboxNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -2060759178285536334L;

	public PaymentOutboxNotFoundException(String message) {
		super(message);
	}
}
