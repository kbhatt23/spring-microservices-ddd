package com.learning.restrauntdomaincore.exceptions;

import com.learning.commondomain.exceptions.DomainException;

public class RestrauntNotFoundException extends DomainException {

	private static final long serialVersionUID = -6859096671238212191L;

	public RestrauntNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public RestrauntNotFoundException(String message) {
		super(message);
	}

}
