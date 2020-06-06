package com.coupon.kakaopay.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel {

	private String timestamp;

	private String path;

	private Integer status;

	private String message;
	
}
