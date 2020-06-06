package com.coupon.kakaopay.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coupon.kakaopay.exception.BadRequestException;
import com.coupon.kakaopay.model.dto.Coupon;
import com.coupon.kakaopay.model.dto.User;
import com.coupon.kakaopay.model.dto.UserCoupon;
import com.coupon.kakaopay.model.request.CouponPayment;
import com.coupon.kakaopay.model.type.CouponStatus;
import com.coupon.kakaopay.repository.CouponRepository;
import com.coupon.kakaopay.repository.UserCouponRepository;
import com.coupon.kakaopay.repository.UserRepository;

@Service
public class UserCouponService {

	@Autowired
	private UserCouponRepository userCouponRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CouponRepository couponRepository;

	@Value("${coupon.period}")
	private Long period;

	@Transactional(readOnly = true)
	public Optional<UserCoupon> getBySerial(Long serial) {
		return userCouponRepository.findById(serial);
	}

	@Transactional(readOnly = true)
	public List<UserCoupon> getAllByUser(User user) {
		return userCouponRepository.findAllByUser(user);
	}

	@Transactional
	public UserCoupon save(UserCoupon userCoupon) {
		return userCouponRepository.save(userCoupon);
	}

	@Transactional
	public void updateUserCouponStatus(UserCoupon userCoupon) {
		userCouponRepository.updateUserCouponStatus(userCoupon);
	}

	@Transactional
	public void deleteBySerial(Long serial) {
		userCouponRepository.deleteById(serial);
	}

	@Transactional(readOnly = true)
	public List<UserCoupon> getUserCouponListByExpiryDate(LocalDate expiryDate) {
		return userCouponRepository.findAllByExpiryDate(expiryDate);
	}

	// ----------------------------------------------------

	@Transactional
	public UserCoupon createUserCoupon(Long serial) {
		User user = getUser(serial);
		Coupon coupon = getCoupon();

		UserCoupon userCoupon = giveToUserCoupon(user, coupon);
		markUserCoupon(coupon, userCoupon);

		return userCoupon;
	}

	@Cacheable(value = "userCouponList", key = "#serial")
	@Transactional(readOnly = true)
	public List<UserCoupon> getUserCouponList(Long serial) {
		User user = getUser(serial);
		List<UserCoupon> userCouponList = userCouponRepository.findAllByUser(user);
		if (userCouponList.isEmpty())
			throw new BadRequestException("Your coupon cannot be found.");

		return userCouponList;
	}

	@CachePut(value = "userCouponList", key = "#serial")
	@Transactional
	public UserCoupon updateCouponPayment(Long serial, CouponPayment couponPayment) {
		User user = getUser(serial);
		Coupon coupon = getCouponByCode(couponPayment.getCouponCode());
		UserCoupon userCoupon = getUserCoupon(user, coupon);

		return updateCouponStatus(couponPayment, userCoupon);
	}

	@Cacheable(value = "expiredOnTheDay", key = "#expiryDate")
	@Transactional(readOnly = true)
	public Page<UserCoupon> getUserCouponListByStatusAndExpiryDate(CouponStatus status, LocalDate expiryDate,
			Pageable pageable) {
		return userCouponRepository.findAllByStatusAndExpiryDate(status, expiryDate, pageable);
	}

	// -----------------------------------------------------

	@Transactional(readOnly = true)
	public User getUser(Long serial) {
		Optional<User> user = userRepository.findById(serial);
		user.orElseThrow(() -> new BadRequestException("The user cannot be found."));
		return user.get();
	}

	@Transactional(readOnly = true)
	public Coupon getCoupon() {
		Page<Coupon> coupons = couponRepository.findByUserCouponIsNull(PageRequest.of(0, 1));
		if (coupons.isEmpty())
			throw new BadRequestException("There are no coupons. Please create a coupon.");

		Optional<Coupon> coupon = coupons.stream().findFirst();
		coupon.orElseThrow(() -> new NoSuchElementException());
		return coupon.get();
	}

	@Transactional(readOnly = true)
	public Coupon getCouponByCode(String code) {
		Optional<Coupon> coupon = couponRepository.findById(code);
		coupon.orElseThrow(() -> new BadRequestException("Invalid coupon code."));
		return coupon.get();
	}

	@Transactional(readOnly = true)
	public UserCoupon getUserCoupon(User user, Coupon coupon) {
		Optional<UserCoupon> userCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
		userCoupon.orElseThrow(() -> new BadRequestException("The requested coupon cannot be found."));
		return userCoupon.get();
	}

	public UserCoupon giveToUserCoupon(User user, Coupon coupon) {
		UserCoupon userCoupon = new UserCoupon(coupon, user, CouponStatus.UNUSED);
		LocalDate startDate = LocalDate.now();
		userCoupon.setStartDate(startDate);
		userCoupon.setExpiryDate(startDate.plusDays(this.period));
		return userCouponRepository.save(userCoupon);
	}

	public void markUserCoupon(Coupon coupon, UserCoupon userCoupon) {
		coupon.setUserCoupon(userCoupon);
		couponRepository.save(coupon);
	}

	public UserCoupon updateCouponStatus(CouponPayment couponPayment, UserCoupon userCoupon) {
		userCoupon.setStatus(couponPayment.getStatus());
		return userCouponRepository.save(userCoupon);
	}

}
