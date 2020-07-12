package com.coupon.kakaopay.contorller;

import static org.assertj.core.api.Assertions.assertThat;

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

import com.coupon.kakaopay.model.request.CouponPayment;
import com.coupon.kakaopay.model.type.CouponStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerFailTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void createUserCoupon() {
		ResponseEntity<String> response = restTemplate.postForEntity("/api/users/{userId}/coupon", null, String.class,
				100);
		log.info("createUserCoupon : {}", response);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		// NotFoundUser("The user cannot be found.")
		// NeedToCreateCoupons("There are no coupons. Please create a coupon.")
		log.info(response.getBody());
	}

	@Test
	public void getUserCoupons() {
		ResponseEntity<String> response = restTemplate.getForEntity("/api/users/{userId}/coupons", String.class, 100);
		log.info("getUserCoupons : {}", response);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		// NotFoundUser("The user cannot be found.")
		// NotFoundUserCoupon("Your coupon cannot be found.")
		log.info(response.getBody());
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
				request, String.class, 100);
		log.info("updateCouponStatus : {}", response);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		// NotFoundUser("The user cannot be found.")
		// InvalidCouponCode("Invalid coupon code.")
		// NoCouponRequested("The requested coupon cannot be found.")
		log.info(response.getBody());
	}
}
