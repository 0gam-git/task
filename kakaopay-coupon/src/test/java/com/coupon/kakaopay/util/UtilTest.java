package com.coupon.kakaopay.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

public class UtilTest {

	@Test
	public void generateUniqueKey() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String uuid = CodeGenerator.generateUniqueKeysWithUUIDAndMessageDigest();
		System.out.println(uuid);
	}

	@Test
	public void generateType5() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String uuid = CodeGenerator.getType5UUID();
		System.out.println(uuid);
	}
}
