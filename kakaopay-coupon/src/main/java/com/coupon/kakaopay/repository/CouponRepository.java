package com.coupon.kakaopay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coupon.kakaopay.model.dto.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String> {

	Optional<Coupon> findFirstByUserCouponIsNull();

	Optional<Coupon> findByCode(String code);

}
