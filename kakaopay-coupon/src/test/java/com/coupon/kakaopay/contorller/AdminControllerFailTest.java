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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.coupon.kakaopay.exception.type.ExceptionMessageType;
import com.coupon.kakaopay.model.response.ResponseModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminControllerFailTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void generatedAndStoreCoupons_InvalidNumberSize_min() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, Integer> map = new HashMap<>();
		map.put("count", -1);

		HttpEntity<Map<String, Integer>> request = new HttpEntity<>(map, headers);

		ResponseEntity<ResponseModel> response = restTemplate.postForEntity("/api/admin/settings/coupon/generator",
				request, ResponseModel.class);
		log.info("generatedAndStoreCoupons : {}", response);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody().getMessage()).isEqualTo(ExceptionMessageType.InvalidNumberSize.getMessage());
	}

	@Test
	public void generatedAndStoreCoupons_InvalidNumberSize_max() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, Integer> map = new HashMap<>();
		map.put("count", 100001);

		HttpEntity<Map<String, Integer>> request = new HttpEntity<>(map, headers);

		ResponseEntity<ResponseModel> response = restTemplate.postForEntity("/api/admin/settings/coupon/generator",
				request, ResponseModel.class);
		log.info("generatedAndStoreCoupons : {}", response);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody().getMessage()).isEqualTo(ExceptionMessageType.InvalidNumberSize.getMessage());
	}

}
