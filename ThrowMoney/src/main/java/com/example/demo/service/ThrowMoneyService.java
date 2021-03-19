package com.example.demo.service;

import com.example.demo.model.ThrowMoney;

public interface ThrowMoneyService {

	void addThrowingMoney(int userId, String roomId, int amountPaid, int headCount, String key);

	ThrowMoney getThrowingMoney(String key);

	void updateThrowingMoney(int userId, String roomId, String token, int randomMoney);

}
