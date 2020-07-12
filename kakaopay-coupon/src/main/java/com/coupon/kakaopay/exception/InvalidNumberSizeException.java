package com.coupon.kakaopay.exception;

public class InvalidNumberSizeException extends RuntimeException {
	
	private static final long serialVersionUID = -4329676986441980271L;

	public InvalidNumberSizeException(String message) {
		super(message);
	}

	public InvalidNumberSizeException(String message, Throwable cause) {
		super(message, cause);
	}
}
