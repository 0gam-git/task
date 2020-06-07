package com.coupon.kakaopay.contorller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.coupon.kakaopay.model.response.ResponseModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void generatedAndStoreCoupons() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, Integer> map = new HashMap<>();
		map.put("count", 100000);

		HttpEntity<Map<String, Integer>> request = new HttpEntity<>(map, headers);

		ResponseEntity<ResponseModel> response = restTemplate.postForEntity("/api/admin/settings/coupon/generator",
				request, ResponseModel.class);
		log.info("generatedAndStoreCoupons : {}", response);
		assertThat(response).isNotNull();
	}

	@Test
	public void getExpiredCouponListWithPage() {
		ResponseEntity<String> response = restTemplate.getForEntity("/api/admin/expired/coupons", String.class);
		log.info("getExpiredCouponListWithPage : {}", response);
		assertThat(response).isNotNull();
	}
}
