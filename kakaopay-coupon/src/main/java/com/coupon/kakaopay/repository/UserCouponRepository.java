package com.coupon.kakaopay.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coupon.kakaopay.model.dto.Coupon;
import com.coupon.kakaopay.model.dto.User;
import com.coupon.kakaopay.model.dto.UserCoupon;
import com.coupon.kakaopay.model.type.CouponStatus;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

	List<UserCoupon> findAllByUser(User user);

	Optional<UserCoupon> findByUserAndCoupon(User user, Coupon coupon);

	Page<UserCoupon> findAllByStatusAndExpiryDate(CouponStatus status, LocalDate expiryDate, Pageable pageable);

	List<UserCoupon> findAllByExpiryDate(LocalDate expiryDate);

	List<UserCoupon> findAllByUserAndExpiryDate(User user, LocalDate expiryDate);

	@Modifying
	@Query(value = "UPDATE UserCoupon SET status = :#{#userCoupon.status} WHERE serial = :#{#userCoupon.serial}")
	void updateUserCouponStatus(@Param("userCoupon") UserCoupon userCoupon);

}
