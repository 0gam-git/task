package com.example.demo.util;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Random;

public class RandomMoneyUtil {

	public static PriorityQueue<Integer> getRandomDistribution(int amountPaid, int headCount) {
		Random r = new Random();
		int low = 1;
		int high = amountPaid;

		PriorityQueue<Integer> queue = new PriorityQueue<>(Collections.reverseOrder());

		for (int i = 0; i < headCount; i++) {
			if (i == (headCount - 1)) {
				int leftOverMoney = high;
				queue.add(leftOverMoney);
				break;
			}

			if (high == low) {
				queue.add(high);

				int peek = queue.poll();
				high = peek;
			}

			int randomDistribution = r.nextInt(high - low) + low;
			queue.add(randomDistribution);

			high = high - randomDistribution;
		}

		return queue;
	}
}
