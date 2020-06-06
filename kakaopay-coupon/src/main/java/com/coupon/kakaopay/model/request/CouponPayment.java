package com.coupon.kakaopay.model.request;

import java.io.Serializable;

import com.coupon.kakaopay.model.type.CouponStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponPayment implements Serializable {
	private static final long serialVersionUID = -275613482193276392L;

	@NonNull
	private String CouponCode;

	@NonNull
	private CouponStatus status;

}
