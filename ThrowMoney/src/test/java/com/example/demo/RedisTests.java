package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.component.RedisUtil;

@SpringBootTest
public class RedisTests {

	private final String ROOM_ID = "test-room";

	@Autowired
	private RedisUtil<String> redisUtil;

	@Test
	public void isAvailableTest() {
		assertTrue(redisUtil.isAvailable());
	}

	@Test
	public void getTimeTest() {
		// unix time in seconds.
		long date = redisUtil.getTime().longValue();
		Date d = new Date(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.KOREAN);
		String format = sdf.format(d);
		System.out.println("SimpleDateFormat : " + format);
	}

	@Test
	public void putAndGetTest() {
		String key = "testKey";
		String value = "testStringValue";

		redisUtil.putValue(key, value);
		String returnValue = redisUtil.getValue(key);

		assertEquals(value, returnValue);
	}

	/**
	 * 분배한 돈을 가져가는 구조로 저장
	 * 
	 * 만료 시간 10분
	 */
	@Test
	public void listTest() {
		String token = RandomStringUtils.random(3);
		token = Base64.getEncoder().encodeToString(token.getBytes());
		System.out.println("token : " + token);

		String key = "room:" + ROOM_ID + ":throwing-money:receipt:" + token;

		redisUtil.addList(key, "test1");
		redisUtil.addList(key, "test2");
		redisUtil.addList(key, "test3");

		// 값 넣고 만료 시간
		redisUtil.setExpire(key, 10, TimeUnit.MINUTES);

		List<String> list = redisUtil.getListMembers(key);

		for (String s : list) {
			System.out.println(s);
		}
	}

	/**
	 * pop 형태로 "받기 사용자" 요청 시 금액을 전달한다.
	 * 
	 * 만료 시간 10분 확인
	 * 
	 * 다 꺼내면 만료
	 */
	@Test
	public void popTest() {
		String token = "6KiM8Ke9og==";
		String key = "room:" + ROOM_ID + ":throwing-money:receipt:" + token;

		System.out.println("before size : " + redisUtil.getListSize(key));

		// 만료되면 -2
		// 10분 이내로 전부 다 받아갈 경우, 만료되었어도 -2 => 실패 응답
		Long minutes = redisUtil.getExpire(key, TimeUnit.MINUTES);
		System.out.println("minutes : " + minutes);
		assertThat(minutes).isGreaterThan(-2);

		// 없으면 null
		String value = redisUtil.leftPop(key);
		System.out.println(value);
		System.out.println("after size : " + redisUtil.getListSize(key));
	}

	@Test
	public void expireTest() {
		String token = "6KiM8Ke9og==";
		String key = "room:" + ROOM_ID + ":throwing-money:receipt:" + token;

		Long seconds = redisUtil.getExpire(key);
		Long minutes = redisUtil.getExpire(key, TimeUnit.MINUTES);
		assertThat(minutes).isGreaterThan(-2);
		System.out.println("seconds : " + seconds);
		System.out.println("minutes : " + minutes);
	}

}
