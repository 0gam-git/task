package com.coupon.kakaopay.exception;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = -437276824168955829L;

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
