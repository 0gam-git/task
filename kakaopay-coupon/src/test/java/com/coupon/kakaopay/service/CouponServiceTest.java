package com.coupon.kakaopay.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.coupon.kakaopay.model.dto.Coupon;
import com.coupon.kakaopay.repository.CouponRepository;
import com.coupon.kakaopay.service.batch.CouponBatchService;
import com.coupon.kakaopay.util.CodeGenerator;

@SpringBootTest
public class CouponServiceTest {

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private CouponBatchService couponBatchService;

	@Test
	public void 쿠폰_N개_만들기_JDBC_테스트() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		int input = 100000;
		couponBatchService.createCouponByCount(input);
	}

	@Test
	public void 등록되지_않은_쿠폰_하나_가져오기_테스트() {
		Page<Coupon> coupons = couponRepository.findByUserCouponIsNull(PageRequest.of(0, 1));
		assertThat(coupons).isNotEmpty();

		Optional<Coupon> coupon = coupons.stream().findFirst();
		assertThat(coupon).isNotEmpty();
	}

	// -------------------------------------------------

	public void 쿠폰_N개_만들기_JPA_테스트() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		int input = 100000;
		List<Coupon> couponList = createByCount(input);
		assertEquals(input, couponList.size());
	}

	public List<Coupon> createByCount(int n) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		List<Coupon> newCouponList = new ArrayList<Coupon>(n);

		for (int i = 0; i < n; i++) {
			Coupon coupon = new Coupon();
			coupon.setCode(CodeGenerator.getType5UUID());
			newCouponList.add(coupon);
		}

		return couponRepository.saveAll(newCouponList);
	}

}
