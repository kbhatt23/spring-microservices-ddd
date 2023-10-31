package com.learning.restrauntdataaccess.exceptions;

public class OrderOutboxNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -6161507855593079646L;

	public OrderOutboxNotFoundException(String message) {
		super(message);
	}
}
