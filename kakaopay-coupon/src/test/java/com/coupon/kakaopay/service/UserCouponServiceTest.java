package com.coupon.kakaopay.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.coupon.kakaopay.model.dto.Coupon;
import com.coupon.kakaopay.model.dto.User;
import com.coupon.kakaopay.model.dto.UserCoupon;
import com.coupon.kakaopay.model.type.CouponStatus;
import com.coupon.kakaopay.service.batch.CouponBatchService;
import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
public class UserCouponServiceTest {

	@Autowired
	private UserCouponService userCouponService;

	@Autowired
	private UserService userService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private CouponBatchService couponBatchService;

	public void 날짜_테스트() {
		LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
		assertThat(today).isEqualTo("2020-06-05");

		LocalDate nextDay = today.plusDays(3);
		assertThat(nextDay).isEqualTo("2020-06-08");
	}

	@Test
	public void 유저_쿠폰_발급_테스트() {
		String email = "dudwns519@daum.net";
		Optional<User> user = userService.getByEmail(email);
		assertThat(user).isNotEmpty();

		Page<Coupon> coupons = couponService.getCoupons(PageRequest.of(0, 1));
		assertThat(coupons).isNotEmpty();

		Optional<Coupon> coupon = coupons.stream().findFirst();
		coupon.orElseThrow(() -> new NoSuchElementException());
		Coupon c = coupon.get();

		UserCoupon userCoupon = new UserCoupon(c, user.get(), CouponStatus.UNUSED);
		LocalDate startDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
		userCoupon.setStartDate(startDate);
		userCoupon.setExpiryDate(startDate.plusDays(6));

		userCouponService.save(userCoupon);

		c.setUserCoupon(userCoupon);
		couponService.save(c);
	}

	@Test
	public void 유저_쿠폰_조회_테스트() throws JsonProcessingException {
		Optional<User> user = userService.getBySerial(1L);
		assertThat(user).isNotEmpty();

		List<UserCoupon> userCouponList = userCouponService.getAllByUser(user.get());
		assertThat(userCouponList).isNotEmpty();
	}

	@Test
	public void 유저_쿠폰_사용_테스트() {
		Optional<User> user = userService.getBySerial(1L);
		assertThat(user).isNotEmpty();

		List<UserCoupon> userCouponList = userCouponService.getAllByUser(user.get());
		assertThat(userCouponList).isNotEmpty();

		UserCoupon userCoupon = userCouponList.get(0);
		userCoupon.setStatus(CouponStatus.USED);

		userCouponService.updateUserCouponStatus(userCoupon);
	}

	@Test
	public void 유저_쿠폰_사용_취소_테스트() {
		Optional<User> user = userService.getBySerial(1L);
		assertThat(user).isNotEmpty();

		List<UserCoupon> userCouponList = userCouponService.getAllByUser(user.get());
		assertThat(userCouponList).isNotEmpty();

		UserCoupon userCoupon = userCouponList.get(0);

		userCoupon.setStatus(CouponStatus.UNUSED);
		userCouponService.updateUserCouponStatus(userCoupon);
	}

	@Test
	public void 오늘이_만료인_쿠폰_조회_테스트() {
		LocalDate localDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
		List<UserCoupon> list = userCouponService.getUserCouponListByExpiryDate(localDate);
		assertThat(list).isNotEmpty();
	}

	@Test
	public void 만료일_변경_테스트() {
		LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
		List<UserCoupon> list = userCouponService.getUserCouponListByExpiryDate(today.minusDays(1));
		assertThat(list).isNotEmpty();

		for (UserCoupon userCoupon : list) {
			userCoupon.setStatus(CouponStatus.EXPIRED);
		}

		couponBatchService.updateUserCoupon(list);
	}

	@Test
	public void 오늘이_만료일이면서_만료상태인_쿠폰_조회_테스트() {
		LocalDate localDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
		Page<UserCoupon> userCoupons = userCouponService.getUserCouponListByStatusAndExpiryDate(CouponStatus.EXPIRED,
				localDate, PageRequest.of(0, 10));
		assertThat(userCoupons).isNotEmpty();
	}

	// ------------------------------------------

	@Test
	public void createUserCoupon() {
		UserCoupon userCoupon = userCouponService.createUserCoupon(1L);
		assertThat(userCoupon).isNotNull();
	}

}
