package com.example.demo.service.Impl;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.example.demo.component.RedisUtil;
import com.example.demo.service.CommonService;

@Service
public class CommonServiceImpl implements CommonService {

	private static final int EXPIRE_DAYS = 7;
	private static final int EXPIRE_MINUTES = 10;

	private RedisUtil<String> redisUtil;

	public CommonServiceImpl(RedisUtil<String> redisUtil) {
		this.redisUtil = redisUtil;
	}

	@Override
	public Long isExpire(String key, TimeUnit timeUnit) {
		return redisUtil.getExpire(key, timeUnit);
	}

	@Override
	public void addExpireDays(String key) {
		redisUtil.setExpire(key, EXPIRE_DAYS, TimeUnit.DAYS);
	}

	@Override
	public void addExpireMinutes(String key) {
		redisUtil.setExpire(key, EXPIRE_MINUTES, TimeUnit.MINUTES);
	}
}
