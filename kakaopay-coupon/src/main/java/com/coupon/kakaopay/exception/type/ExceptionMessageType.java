package com.coupon.kakaopay.exception.type;

public enum ExceptionMessageType {
	InvalidNumberSize("The number requested is too large or too small."),
	NotFoundUserCoupon("Your coupon cannot be found."),
	NotFoundUser("The user cannot be found."),
	NeedToCreateCoupons("There are no coupons. Please create a coupon."),
	InvalidCouponCode("Invalid coupon code."),
	NoCouponRequested("The requested coupon cannot be found.")
	
	;

	private String message;

	private ExceptionMessageType(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
