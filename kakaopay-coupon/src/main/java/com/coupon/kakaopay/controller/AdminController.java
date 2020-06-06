package com.coupon.kakaopay.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.coupon.kakaopay.exception.InvalidNumberSizeException;
import com.coupon.kakaopay.model.dto.UserCoupon;
import com.coupon.kakaopay.model.response.ResponseModel;
import com.coupon.kakaopay.model.type.CouponStatus;
import com.coupon.kakaopay.service.UserCouponService;
import com.coupon.kakaopay.service.batch.CouponBatchService;
import com.coupon.kakaopay.util.DateUtil;
import com.coupon.kakaopay.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/admin")
public class AdminController {

	@Autowired
	private UserCouponService userCouponService;

	@Autowired
	private CouponBatchService couponBatchService;

	/**
	 * N coupons with random codes are generated and stored in the database.
	 */
	@PostMapping(value = "/settings/coupon/generator")
	public ResponseModel generatedAndStoreCoupons(WebRequest req, Integer count)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		log.info("## SettingController - generatedAndStoreCoupons(count : {})", count);

		if (1000000 > count || 0 > count) {
			throw new InvalidNumberSizeException("The number requested is too large or too small.");
		}

		couponBatchService.createCouponByCount(count);

		ResponseModel model = new ResponseModel();
		model.setTimestamp(DateUtil.getTimestamp());
		model.setPath(ResponseUtil.getUri(req));
		model.setStatus(HttpStatus.OK.hashCode());
		model.setMessage("N coupons with random codes are generated and stored in the database.");

		return model;
	}

	/**
	 * List of all coupons that have expired on the day.
	 */
	@DeleteMapping(value = "/expired/coupons")
	public Page<UserCoupon> getExpiredCouponListWithPage(Pageable pageable) {
		return userCouponService.getUserCouponListByStatusAndExpiryDate(CouponStatus.EXPIRED,
				LocalDate.now(ZoneId.of("Asia/Seoul")), pageable);
	}

}
