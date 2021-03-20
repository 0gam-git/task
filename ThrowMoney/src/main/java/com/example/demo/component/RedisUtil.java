package com.example.demo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtil<T> {
	private RedisTemplate<String, T> redisTemplate;
	private ValueOperations<String, T> valueOperations;
	private ListOperations<String, T> listOperations;

	@Autowired
	public RedisUtil(RedisTemplate<String, T> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.valueOperations = redisTemplate.opsForValue();
		this.listOperations = redisTemplate.opsForList();
	}

	// String
	public void putValue(String key, T value) {
		valueOperations.set(key, value);
	}

	public void putValue(String key, T value, long timeout, TimeUnit unit) {
		valueOperations.set(key, value, timeout, unit);
	}

	public T getValue(String key) {
		return valueOperations.get(key);
	}

	// expire
	public void setExpire(String key, long timeout, TimeUnit unit) {
		redisTemplate.expire(key, timeout, unit);
	}

	public Long getExpire(String key, TimeUnit unit) {
		return redisTemplate.getExpire(key, unit);
	}

	public Long getExpire(String key) {
		return redisTemplate.getExpire(key);
	}

	// List
	public void addList(String key, T value) {
		listOperations.leftPush(key, value);
	}

	public void addListAll(String key, Collection<T> values) {
		listOperations.leftPushAll(key, values);
	}

	public List<T> getListMembers(String key) {
		return listOperations.range(key, 0, -1);
	}

	public Long getListSize(String key) {
		return listOperations.size(key);
	}

	public T leftPop(String key) {
		return listOperations.leftPop(key);
	}

	public Boolean isAvailable() {
		try {
			String status = redisTemplate.getConnectionFactory().getConnection().ping();

			if (status != null) {
				return true;
			}

		} catch (Exception e) {
			log.warn("Redis server is not available at the moment.");
		}

		return false;
	}

	public Long getTime() {
		return redisTemplate.getConnectionFactory().getConnection().time();
	}
}