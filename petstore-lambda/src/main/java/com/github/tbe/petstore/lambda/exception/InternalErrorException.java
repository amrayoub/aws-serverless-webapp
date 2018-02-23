package com.github.tbe.petstore.lambda.exception;

public class InternalErrorException extends RuntimeException {

	private static final long serialVersionUID = 5290330945944715973L;
	private static final String PREFIX = "INT_ERROR: ";

	public InternalErrorException(String message, Throwable cause) {
		super(PREFIX + message, cause);
	}

	public InternalErrorException(String message) {
		super(PREFIX + message);
	}

	public InternalErrorException(Throwable cause) {
		super(PREFIX + cause.getMessage(), cause);
	}
}
