package com.example.demo.util;

public class RedisKeyUtil {

	public static String getRestrictedUserListKey(String roomId, String token) {
		return "room:" + roomId + ":throwing-money:restriction-list:" + token;
	}

	public static String getReceiptKey(String roomId, String token) {
		return "room:" + roomId + ":throwing-money:receipt:" + token;
	}

	public static String getLookupKey(String roomId, String token) {
		return "room:" + roomId + ":throwing-money:lookup:" + token;
	}

}
