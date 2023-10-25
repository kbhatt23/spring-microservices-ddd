package com.learning.restrauntdomaincore.exceptions;

import com.learning.commondomain.exceptions.DomainException;

public class RestrauntDomainException extends DomainException {

	private static final long serialVersionUID = 2349977987613153816L;

	public RestrauntDomainException(String message) {
		super(message);
	}

	public RestrauntDomainException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
