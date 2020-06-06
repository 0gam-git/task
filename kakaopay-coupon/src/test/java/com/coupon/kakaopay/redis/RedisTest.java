package com.coupon.kakaopay.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

//	@Autowired
//	RedisTemplate<String, Object> redisTemplate;
//
//	@Test
//	public void 레디스_기본_테스트() {
//		String key = "key";
//		String value = "test123";
//
//		redisTemplate.opsForValue().set(key, value);
//
//		Object object =  redisTemplate.opsForValue().get(key);
//		
//		System.out.println("!");
//	}
}
