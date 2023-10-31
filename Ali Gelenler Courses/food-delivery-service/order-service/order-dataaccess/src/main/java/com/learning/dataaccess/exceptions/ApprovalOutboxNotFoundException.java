package com.learning.dataaccess.exceptions;

public class ApprovalOutboxNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7878658455307487945L;

	public ApprovalOutboxNotFoundException(String message) {
		super(message);
	}
}