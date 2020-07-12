package com.coupon.kakaopay.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private final static SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getTimestamp() {
		return TIMESTAMP_FORMAT.format(new Date());
	}
}
