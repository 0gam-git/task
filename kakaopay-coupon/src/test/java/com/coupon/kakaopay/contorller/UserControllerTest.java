package com.coupon.kakaopay.contorller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.coupon.kakaopay.model.dto.Coupon;
import com.coupon.kakaopay.model.dto.User;
import com.coupon.kakaopay.model.request.CouponPayment;
import com.coupon.kakaopay.model.type.CouponStatus;
import com.coupon.kakaopay.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private UserService userService;

	private Long userId;

	@BeforeEach
	public void setUp() {
		String dummy = "test@kakaopay.com";
		Optional<User> oUser = userService.getByEmail(dummy);
		User user = null;

		if (!oUser.isPresent()) {
			user = new User();
			user.setEmail(dummy);
			user.setPassword("dummy");
			userService.save(user);
		} else {
			user = oUser.get();
		}

		userId = user.getSerial();
	}

	@Test
	public void createUserCoupon() {
		ResponseEntity<Coupon> response = restTemplate.postForEntity("/api/users/{userId}/coupon", null, Coupon.class,
				userId);
		log.info("createUserCoupon : {}", response);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		log.info("coupon code : {}", response.getBody().getCode());
	}

	@Test
	public void getUserCoupons() {
		ResponseEntity<String> response = restTemplate.getForEntity("/api/users/{userId}/coupons", String.class,
				userId);
		log.info("getUserCoupons : {}", response);
		assertThat(response).isNotNull();
	}

	@Test
	public void updateCouponStatus() {
		// getUserCoupons > get coupon > input coupon code, status
		CouponPayment payment = new CouponPayment();
		payment.setCouponCode("17a6eef5-0b63-5c9a-9e8d-8e503ec07650");
		payment.setStatus(CouponStatus.USED);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<CouponPayment> request = new HttpEntity<>(payment, headers);

		ResponseEntity<String> response = restTemplate.exchange("/api/users/{userId}/coupon/status", HttpMethod.PATCH,
				request, String.class, userId);
		log.info("updateCouponStatus : {}", response);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}
