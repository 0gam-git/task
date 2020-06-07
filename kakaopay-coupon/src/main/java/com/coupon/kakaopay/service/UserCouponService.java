package com.coupon.kakaopay.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coupon.kakaopay.exception.BadRequestException;
import com.coupon.kakaopay.exception.type.ExceptionMessageType;
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
	public Coupon createUserCoupon(Long serial) {
		User user = getUser(serial);
		Coupon coupon = getCoupon();

		UserCoupon userCoupon = giveToUserCoupon(user, coupon);

		return markUserCoupon(coupon, userCoupon);
	}

	@Transactional(readOnly = true)
	public List<UserCoupon> getUserCouponList(Long serial) {
		User user = getUser(serial);
		List<UserCoupon> userCouponList = userCouponRepository.findAllByUser(user);
		if (userCouponList.isEmpty())
			throw new BadRequestException(ExceptionMessageType.NotFoundUserCoupon.getMessage());

		return userCouponList;
	}

	@Transactional
	public void updateCouponPayment(Long serial, CouponPayment couponPayment) {
		User user = getUser(serial);
		Coupon coupon = getCouponByCode(couponPayment.getCouponCode());
		UserCoupon userCoupon = getUserCoupon(user, coupon);

		userCoupon.setStatus(couponPayment.getStatus());
		userCouponRepository.updateUserCouponStatus(userCoupon);
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
		user.orElseThrow(() -> new BadRequestException(ExceptionMessageType.NotFoundUser.getMessage()));
		return user.get();
	}

	@Transactional(readOnly = true)
	public Coupon getCoupon() {
		Page<Coupon> coupons = couponRepository.findByUserCouponIsNull(PageRequest.of(0, 1));
		if (coupons.isEmpty())
			throw new BadRequestException(ExceptionMessageType.NeedToCreateCoupons.getMessage());

		return coupons.stream().findFirst().get();
	}

	@Transactional(readOnly = true)
	public Coupon getCouponByCode(String code) {
		Optional<Coupon> coupon = couponRepository.findByCode(code);
		coupon.orElseThrow(() -> new BadRequestException(ExceptionMessageType.InvalidCouponCode.getMessage()));
		return coupon.get();
	}

	@Transactional(readOnly = true)
	public UserCoupon getUserCoupon(User user, Coupon coupon) {
		Optional<UserCoupon> userCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
		userCoupon.orElseThrow(() -> new BadRequestException(ExceptionMessageType.NoCouponRequested.getMessage()));
		return userCoupon.get();
	}

	public UserCoupon giveToUserCoupon(User user, Coupon coupon) {
		UserCoupon userCoupon = new UserCoupon(coupon, user, CouponStatus.UNUSED);
		LocalDate startDate = LocalDate.now();
		userCoupon.setStartDate(startDate);
		userCoupon.setExpiryDate(startDate.plusDays(this.period));
		return userCouponRepository.save(userCoupon);
	}

	public Coupon markUserCoupon(Coupon coupon, UserCoupon userCoupon) {
		coupon.setUserCoupon(userCoupon);
		return couponRepository.save(coupon);
	}

}
