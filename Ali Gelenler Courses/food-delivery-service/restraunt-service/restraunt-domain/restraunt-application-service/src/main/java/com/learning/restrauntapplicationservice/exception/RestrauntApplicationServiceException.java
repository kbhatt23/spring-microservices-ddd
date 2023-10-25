package com.learning.restrauntapplicationservice.exception;

import com.learning.commondomain.exceptions.DomainException;

public class RestrauntApplicationServiceException extends DomainException {

	private static final long serialVersionUID = 4619555011600067898L;

	public RestrauntApplicationServiceException(String message) {
		super(message);
	}

	public RestrauntApplicationServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}