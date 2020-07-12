package com.coupon.kakaopay.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.coupon.kakaopay.model.dto.User;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Test
	public void 유저_생성_테스트() {
		String email = "dudwns519@daum.net";
		Optional<User> user = userService.getByEmail(email);
		assertThat(user).isEmpty();

		User newUser = new User();
		newUser.setEmail(email);
		newUser.setPassword("1q2w3e4r!@");

		userService.save(newUser);
	}

	@Test
	public void 유저_삭제_테스트() {
		Long serial = 1L;
		Optional<User> user = userService.getBySerial(serial);

		assertThat(user).isNotEmpty();
		// cascade.remove => user > userCoupon > coupon
		userService.deleteBySerial(user.get().getSerial());
	}
}
