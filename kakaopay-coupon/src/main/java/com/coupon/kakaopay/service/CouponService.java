package com.coupon.kakaopay.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coupon.kakaopay.model.dto.Coupon;
import com.coupon.kakaopay.repository.CouponRepository;

@Service
public class CouponService {

	@Autowired
	private CouponRepository couponRepository;

	@Transactional(readOnly = true)
	public Optional<Coupon> getCoupons() {
		return couponRepository.findFirstByUserCouponIsNull();
	}

	@Transactional
	public Coupon save(Coupon coupon) {
		return couponRepository.save(coupon);
	}

	@Transactional
	public void deleteByCode(String code) {
		couponRepository.deleteById(code);
	}

}
