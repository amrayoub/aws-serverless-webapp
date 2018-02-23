package com.github.tbe.petstore.lambda.exception;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 4455232698652545110L;
	private static final String PREFIX = "BAD_REQ: ";

	public BadRequestException(String message, Throwable cause) {
		super(PREFIX + message, cause);
	}

	public BadRequestException(String message) {
		super(PREFIX + message);
	}

	public BadRequestException(Throwable cause) {
		super(PREFIX + cause.getMessage(), cause);
	}
}
