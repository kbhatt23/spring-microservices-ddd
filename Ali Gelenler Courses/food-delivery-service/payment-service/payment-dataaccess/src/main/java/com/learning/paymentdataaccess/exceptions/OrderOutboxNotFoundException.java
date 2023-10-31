package com.learning.paymentdataaccess.exceptions;

public class OrderOutboxNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -6511737067171703988L;

	public OrderOutboxNotFoundException(String message) {
		super(message);
	}
}
