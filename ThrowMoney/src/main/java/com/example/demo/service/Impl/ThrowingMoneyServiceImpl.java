package com.example.demo.service.Impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.example.demo.component.RedisUtil;
import com.example.demo.model.Receipt;
import com.example.demo.model.ThrowMoney;
import com.example.demo.service.ThrowMoneyService;
import com.example.demo.util.RedisKeyUtil;

@Service
public class ThrowingMoneyServiceImpl implements ThrowMoneyService {

	private RedisUtil<ThrowMoney> redisUtil;

	public ThrowingMoneyServiceImpl(RedisUtil<ThrowMoney> redisUtil) {
		this.redisUtil = redisUtil;
	}

	@Override
	public void addThrowingMoney(int userId, String roomId, int amountPaid, int headCount, String key) {
		ThrowMoney model = new ThrowMoney();
		model.setCreatedById(userId);
		model.setAmountPaid(amountPaid);
		model.setHeadCount(headCount);
		model.setStartDateTime(ZonedDateTime.now(ZoneId.of("UTC")));

		redisUtil.putValue(key, model);
	}

	@Override
	public ThrowMoney getThrowingMoney(String key) {
		return redisUtil.getValue(key);
	}

	@Override
	public void updateThrowingMoney(int userId, String roomId, String token, int randomMoney) {
		String lookupKey = RedisKeyUtil.getLookupKey(roomId, token);
		ThrowMoney model = redisUtil.getValue(lookupKey);

		Receipt receipt = new Receipt();
		receipt.setUserId(userId);
		receipt.setAmountReceipt(randomMoney);

		List<Receipt> Receipts = model.getReceipts();
		Receipts.add(receipt);

		int amountReceipt = model.getAmountReceipt() == 0 ? model.getAmountPaid() : model.getAmountReceipt();
		amountReceipt = amountReceipt - randomMoney;

		model.setAmountReceipt(amountReceipt);
		model.setReceipts(Receipts);

		Long renewal = redisUtil.getExpire(lookupKey, TimeUnit.SECONDS);
		redisUtil.putValue(lookupKey, model, renewal, TimeUnit.SECONDS);
	}

}
