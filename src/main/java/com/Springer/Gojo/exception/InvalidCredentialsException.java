package com.Springer.Gojo.exception;

public class InvalidCredentialsException extends RuntimeException {

	private static final long serialVersionUID = 1l;

	public InvalidCredentialsException(String message) {
		super("Invalid email or password");
	}

}
