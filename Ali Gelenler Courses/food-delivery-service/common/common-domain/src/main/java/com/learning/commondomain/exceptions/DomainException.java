package com.learning.commondomain.exceptions;

public class DomainException extends RuntimeException {

	private static final long serialVersionUID = -1989950098791099080L;

	protected DomainException(String message) {
		super(message);
	}

	protected DomainException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
