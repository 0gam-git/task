package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.component.RedisUtil;
import com.example.demo.model.ThrowMoney;
import com.example.demo.util.RedisKeyUtil;

@SpringBootTest
public class ThrowingTests {

	// X-USER-ID, X-ROOM-ID
	private final int USER_ID = 1;
	private final String ROOM_ID = "room-id-1";

	@Autowired
	private RedisUtil<Object> redisUtil;

	@Test
	public void inputTest() {
		String roomId = Base64.getEncoder().encodeToString(ROOM_ID.getBytes());

		// request => 뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다.
		int amountPaid = 1500;
		int headCount = 8;

		// 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게구현해 주세요.)
		PriorityQueue<Integer> queue = getRandomDistribution(amountPaid, headCount);

		// 뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다
		// token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다.
		String token = RandomStringUtils.random(3);
		token = Base64.getEncoder().encodeToString(token.getBytes());

		// * 조회용 API 준비
		// X-USER-ID, X-ROOM-ID를 이용하여 분배한 돈과 관련 정보를 저장한다.
		ThrowMoney model = new ThrowMoney();
		model.setCreatedById(USER_ID);
		model.setAmountPaid(amountPaid);
		model.setHeadCount(headCount);
		model.setStartDateTime(ZonedDateTime.now(ZoneId.of("UTC")));

		String lookupKey = RedisKeyUtil.getLookupKey(roomId, token);
		redisUtil.putValue(lookupKey, model);

		// 조회 만료 시간 7일
		redisUtil.setExpire(lookupKey, 7, TimeUnit.DAYS);

		// * 받기 API 요청 시 갱신한다. > 받기 완료된 정보, 받기 완료한 금액
		// List<Receipt>, amountReceipt

		// * 빈번히 요청할 받기API 검증용 - 이미 받았거나 받지 못하는(뿌리기 생성자) 유저 리스트 저장
		String restrictionListKey = RedisKeyUtil.getRestrictedUserListKey(roomId, token);

		// 초기 제한 리스트에 뿌리기 등록자 저장
		redisUtil.addList(restrictionListKey, USER_ID);

		// 조회 만료 시간 7일
		redisUtil.setExpire(restrictionListKey, 7, TimeUnit.DAYS);

		// * 받기용 API 준비
		// token과 함께 getRandomDistribution 데이터 저장한다.
		String receiptKey = RedisKeyUtil.getReceiptKey(roomId, token);

		for (Integer amountReceipt : queue) {
			redisUtil.addList(receiptKey, amountReceipt);
		}

		// 받기 만료 시간 10분
		redisUtil.setExpire(receiptKey, 10, TimeUnit.MINUTES);

		// response => token
		System.out.println("## response : " + token);

		// 확인
		Long size = redisUtil.getListSize(receiptKey);
		assertEquals(headCount, size);

		Long minutes = redisUtil.getExpire(receiptKey, TimeUnit.MINUTES);
		assertThat(minutes).isGreaterThan(-2);
		System.out.println(minutes + " minutes left.");

		Object o = redisUtil.getValue(lookupKey);
		assertNotNull(o);
		System.out.println(o);

		List<Object> list = redisUtil.getListMembers(restrictionListKey);
		for (Object userId : list) {
			System.out.println("restriction user : " + userId);
		}
	}

	/**
	 * 검증 : 뿌리기 등록 후 만료 확인 (조회용 7일, 받기용 10분)
	 */
	@Test
	public void confirmExpirationTest() {
		String token = "8KqTneyRjg==";
		String roomId = Base64.getEncoder().encodeToString(ROOM_ID.getBytes());

		String lookupKey = RedisKeyUtil.getLookupKey(roomId, token);
		String receiptKey = RedisKeyUtil.getReceiptKey(roomId, token);
		String restrictionListKey = RedisKeyUtil.getRestrictedUserListKey(roomId, token);

		Long days1 = redisUtil.getExpire(lookupKey, TimeUnit.DAYS);
		Long minutes1 = redisUtil.getExpire(restrictionListKey, TimeUnit.MINUTES);
		Long minutes2 = redisUtil.getExpire(receiptKey, TimeUnit.MINUTES);

		assertThat(days1).isGreaterThan(-2);
		System.out.println("lookup API : " + days1 + " days left.");

		assertThat(minutes1).isGreaterThan(-2);
		System.out.println("restriction-list API : " + minutes1 + " minutes left.");

		assertThat(minutes2).isGreaterThan(-2);
		System.out.println("receiptKey API : " + minutes2 + " minutes left.");

	}

	/**
	 * - 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게구현해 주세요.)
	 * 
	 * 분배는 인원수만큼 랜덤으로 분배하고, 최소값은 1원으로 한다.
	 * 
	 * 분배 후 남은 돈이 최소값과 같은 경우, 분배한 금액 중 가장 큰 금액을 가져와 남은 인원수만큼 다시 분배한다.
	 * 
	 * 검증 : 인원수 만큼 분배했는지, 랜덤으로 분배한 금액의 합이 뿌리기 금액과 같은지
	 */
	@Test
	public void moneyDistributionTest() {
		int amountPaid = 234234;
		int headCount = 50;

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
				int peek = queue.poll();
				queue.add(high);
				high = peek;
			}

			int randomDistribution = r.nextInt(high - low) + low;

			high = high - randomDistribution;
			queue.add(randomDistribution);
		}

		// 확인
		assertEquals(headCount, queue.size());

		int sum = 0;
		for (Integer n : queue) {
			sum = sum(sum, n);
		}

		assertEquals(amountPaid, sum);
	}

	public PriorityQueue<Integer> getRandomDistribution(int amountPaid, int headCount) {
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

	int sum(int n1, int n2) {
		return n1 + n2;
	}

}
