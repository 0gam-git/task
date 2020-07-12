package com.coupon.kakaopay.util;

import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public class ResponseUtil {

	public static String getUri(WebRequest req) {
		return ((ServletWebRequest) req).getRequest().getRequestURI().toString();
	}
}
