package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.component.RedisUtil;
import com.example.demo.model.ThrowMoney;
import com.example.demo.util.RedisKeyUtil;

@SpringBootTest
public class LookupTests {

	// X-USER-ID, X-ROOM-ID
	private final int USER_ID = 1;
	private final String ROOM_ID = "test-room입니다";

	@Autowired
	private RedisUtil<Object> redisUtil;

	@Test
	public void inputTest() {
		String roomId = Base64.getEncoder().encodeToString(ROOM_ID.getBytes());
		// 뿌리기 시 발급된 token을 요청값으로 받습니다.
		// request => token
		String token = "8KObk+K+oQ==";
		String lookupKey = RedisKeyUtil.getLookupKey(roomId, token);

		ThrowMoney m = (ThrowMoney) redisUtil.getValue(lookupKey);

		// 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.
		Long days = redisUtil.getExpire(lookupKey, TimeUnit.DAYS);
		assertThat(days).isGreaterThan(-2);
		System.out.println(days + " days left.");

		// 뿌린 사람만 조회 가능(뿌리기 등록자 확인)
		// 다른사람의 뿌리기건이나 유효하지 않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
		assertEquals(m.getCreatedById(), USER_ID);

		// token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다.
		// 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은 사용자 아이디] 리스트
		// response => 뿌리기 건의 상태 정보
		System.out.println("## response : " + m);
	}
}
