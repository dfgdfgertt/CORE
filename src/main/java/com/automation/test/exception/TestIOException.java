package com.automation.test.exception;

import java.io.IOException;

public class TestIOException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1270366735148386843L;

	public TestIOException() {
		super();
	}
	
	public TestIOException(String message) {
		super(message);
	}
	
	public TestIOException(String message, Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public String getMessage() {	
		Throwable cause = getCause();
		
		if (cause == null) {
			return super.getMessage();
		}
		else {
			return String.format("%s: %s\nRoot cause: %s", cause.getStackTrace()[0].getClassName(), super.getMessage(), cause.getMessage());
		}
	}
}
