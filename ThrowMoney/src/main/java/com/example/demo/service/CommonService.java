package com.example.demo.service;

import java.util.concurrent.TimeUnit;

public interface CommonService {

	Long isExpire(String key, TimeUnit timeUnit);

	void addExpireDays(String key);

	void addExpireMinutes(String key);
}
