package com.example.demo.service;

import java.util.List;

public interface ReceiptService {

	List<Integer> getRestrictedUserList(String key);

	int pickRandomMoney(String key);

	void addRestrictedUser(String key, int userId);

	void distributeRandomMoney(String key, int amountPaid, int headCount);
}
