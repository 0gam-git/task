package com.coupon.kakaopay.component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.coupon.kakaopay.model.dto.UserCoupon;
import com.coupon.kakaopay.model.type.CouponStatus;
import com.coupon.kakaopay.service.UserCouponService;
import com.coupon.kakaopay.service.batch.CouponBatchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CouponStatusScheduler {

	@Autowired
	private UserCouponService userCouponService;

	@Autowired
	private CouponBatchService couponBatchService;

	@Scheduled(cron = "0 0 0 * * *")
	public void updateCouponStatus() {
		// 쿠폰 만료 상태로 변경
		log.info("## CouponStatusScheduler - updateCouponStatus()");
		LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
		LocalDate yesterday = today.minusDays(1);
		List<UserCoupon> userCouponList = userCouponService.getUserCouponListByExpiryDate(yesterday);

		log.info("## UserCouponList - size() : {}", userCouponList.size());
		if (userCouponList.isEmpty()) {
			return;
		}

		for (UserCoupon userCoupon : userCouponList) {
			userCoupon.setStatus(CouponStatus.EXPIRED);
		}

		couponBatchService.updateUserCoupon(userCouponList);
		log.info("## CouponStatusScheduler - update finished");
	}

}
