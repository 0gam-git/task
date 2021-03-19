package com.example.demo.util;

import java.util.Base64;

public class StringUtil {

	public static String getEncodeString(String s) {
		return Base64.getEncoder().encodeToString(s.getBytes());
	}
}
