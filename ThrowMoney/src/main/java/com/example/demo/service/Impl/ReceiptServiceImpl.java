package com.example.demo.service.Impl;

import java.util.List;
import java.util.PriorityQueue;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.demo.component.RedisUtil;
import com.example.demo.service.ReceiptService;
import com.example.demo.util.RandomMoneyUtil;

@Service
public class ReceiptServiceImpl implements ReceiptService {

	private RedisUtil<Integer> redisUtil;

	public ReceiptServiceImpl(RedisUtil<Integer> redisUtil) {
		this.redisUtil = redisUtil;
	}

	@CachePut(value = "RestrictedUserList", key = "#key")
	@Override
	public void addRestrictedUser(String key, int userId) {
		redisUtil.addList(key, userId);
	}

	@Cacheable(value = "RestrictedUserList", key = "#key", unless = "#result == null")
	@Override
	public List<Integer> getRestrictedUserList(String key) {
		return redisUtil.getListMembers(key);
	}

	@Override
	public void distributeRandomMoney(String key, int amountPaid, int headCount) {
		PriorityQueue<Integer> randomMoney = RandomMoneyUtil.getRandomDistribution(amountPaid, headCount);

		for (Integer amountReceipt : randomMoney) {
			redisUtil.addList(key, amountReceipt);
		}
	}

	@Override
	public int pickRandomMoney(String key) {
		return redisUtil.leftPop(key);
	}

}
