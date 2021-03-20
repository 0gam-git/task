package com.example.demo.service.Impl;

import java.util.List;
import java.util.PriorityQueue;

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

	@Override
	public void addRestrictedUser(String key, int userId) {
		redisUtil.addList(key, userId);
	}

	@Override
	public List<Integer> getRestrictedUserList(String key) {
		return redisUtil.getListMembers(key);
	}

	@Override
	public void distributeRandomMoney(String key, int amountPaid, int headCount) {
		PriorityQueue<Integer> randomMoneyBundle = RandomMoneyUtil.getRandomDistribution(amountPaid, headCount);

		redisUtil.addListAll(key, randomMoneyBundle);
	}

	@Override
	public int pickRandomMoney(String key) {
		return redisUtil.leftPop(key);
	}

}
