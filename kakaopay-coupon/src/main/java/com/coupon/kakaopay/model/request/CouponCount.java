package com.coupon.kakaopay.model.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponCount implements Serializable {

	private static final long serialVersionUID = 8307945445638344664L;

	private Integer count;

}
