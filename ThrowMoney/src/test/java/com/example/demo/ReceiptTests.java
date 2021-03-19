package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.component.RedisUtil;
import com.example.demo.model.ThrowMoney;
import com.example.demo.model.Receipt;
import com.example.demo.util.RedisKeyUtil;

@SpringBootTest
public class ReceiptTests {

	// X-USER-ID, X-ROOM-ID
	private final int USER_ID = 2;
	private final String ROOM_ID = "test-room입니다";

	@Autowired
	private RedisUtil<Object> redisUtil;

	@Test
	public void inputTest() {
		// 뿌리기 시 발급된 token을 요청값으로 받습니다.
		// request => token
		String token = "8KObk+K+oQ==";
		String roomId = Base64.getEncoder().encodeToString(ROOM_ID.getBytes());

		// 뿌리기 생성자가 아니거나 + 처음 받아가는 유저여야 한다.
		// 뿌리기 당 한 사용자는 한번만 받을 수 있습니다. (저장된 관련 데이터에 뿌리기 "받은 사용자 리스트" 확인)
		// 자신이 뿌리기한 건은 자신이 받을 수 없습니다. (X-USER-ID 확인 <= 저장된 관련 데이터에 "뿌리기 등록한 사용자" 비교)
		// 뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다. (X-ROOM-ID 확인 <= 저장된 뿌리기 등록된
		// X-ROOM-ID 확인)
		String restrictionListKey = RedisKeyUtil.getRestrictedUserListKey(roomId, token);
		List<Object> restrictionList = redisUtil.getListMembers(restrictionListKey);

		assertFalse(restrictionList.isEmpty());
		assertFalse(restrictionList.contains(USER_ID));

		// 10분간 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다.
		String receiptKey = RedisKeyUtil.getReceiptKey(roomId, token);
		Long minutes = redisUtil.getExpire(receiptKey, TimeUnit.MINUTES);
		assertThat(minutes).isGreaterThan(-2);

		// token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를 API를 호출한 사용자에게 할당한다.
		int randomMoney = (int) redisUtil.leftPop(receiptKey);

		// * 받기 API 요청 시 갱신한다. > 받기 완료된 정보, 받기 완료한 금액
		// List<Receipt>, amountReceipt
		String lookupKey = RedisKeyUtil.getLookupKey(roomId, token);
		ThrowMoney model = (ThrowMoney) redisUtil.getValue(lookupKey);

		Receipt receipt = new Receipt();
		receipt.setUserId(USER_ID);
		receipt.setAmountReceipt(randomMoney);

		List<Receipt> Receipts = model.getReceipts();
		Receipts.add(receipt);

		int amountReceipt = model.getAmountReceipt() == 0 ? model.getAmountPaid() : model.getAmountReceipt();
		amountReceipt = amountReceipt - randomMoney;

		model.setAmountReceipt(amountReceipt);
		model.setReceipts(Receipts);

		// 만료 기존 것으로 갱신
		Long renewal = redisUtil.getExpire(lookupKey, TimeUnit.SECONDS);
		redisUtil.putValue(lookupKey, model, renewal, TimeUnit.SECONDS);

		redisUtil.addList(restrictionListKey, USER_ID);

		// 그 금액을 응답값으로 내려줍니다.
		// response => money(randomMoney)
		System.out.println("## response : " + randomMoney);

		// 확인
		ThrowMoney m = (ThrowMoney) redisUtil.getValue(lookupKey);
		assertNotNull(m);
		System.out.println(m);
		assertEquals(amountReceipt, m.getAmountReceipt());
	}

}
