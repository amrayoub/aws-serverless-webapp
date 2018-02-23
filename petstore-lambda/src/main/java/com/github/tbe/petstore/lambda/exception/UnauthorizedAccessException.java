package com.github.tbe.petstore.lambda.exception;

public class UnauthorizedAccessException extends RuntimeException {

	private static final long serialVersionUID = 3888346687792942354L;
	private static final String PREFIX = "UNAUTH_REQ: ";
	
	public UnauthorizedAccessException(String message, Throwable cause) {
		super(PREFIX + message, cause);
	}

	public UnauthorizedAccessException(String message) {
		super(PREFIX + message);
	}

	public UnauthorizedAccessException(Throwable cause) {
		super(PREFIX + cause.getMessage(), cause);
	}
}
