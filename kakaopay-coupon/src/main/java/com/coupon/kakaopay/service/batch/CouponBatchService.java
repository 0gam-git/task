package com.coupon.kakaopay.service.batch;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coupon.kakaopay.model.dto.Coupon;
import com.coupon.kakaopay.model.dto.UserCoupon;
import com.coupon.kakaopay.util.CodeGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CouponBatchService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final int batchSize = 10000;

	@Transactional
	public void createCouponByCount(int n) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		List<Coupon> newCouponList = new ArrayList<Coupon>(n);
		log.info("## CouponService - create by count : {}", n);

		for (int i = 0; i < n; i++) {
			Coupon coupon = new Coupon();
			coupon.setCode(CodeGenerator.getType5UUID());
			newCouponList.add(coupon);
		}

		batchInsert(newCouponList, batchSize);
	}

	@Transactional
	public void updateUserCoupon(List<UserCoupon> userCouponList) {
		batchUpdate(userCouponList, batchSize);
	}

	// ----------------------------------------------------------------------

	public int[][] batchInsert(List<Coupon> coupons, int batchSize) {
		int[][] updateCounts = jdbcTemplate.batchUpdate("insert into coupon (code) values(?)", coupons, batchSize,
				new ParameterizedPreparedStatementSetter<Coupon>() {
					public void setValues(PreparedStatement ps, Coupon argument) throws SQLException {
						ps.setString(1, argument.getCode());
					}
				});

		return updateCounts;
	}

	public int[][] batchUpdate(List<UserCoupon> userCoupons, int batchSize) {
		int[][] updateCounts = jdbcTemplate.batchUpdate("update user_coupon set status = ? where serial = ?",
				userCoupons, batchSize, new ParameterizedPreparedStatementSetter<UserCoupon>() {
					public void setValues(PreparedStatement ps, UserCoupon argument) throws SQLException {
						ps.setInt(1, argument.getStatus().ordinal());
						ps.setLong(2, argument.getSerial());
					}
				});

		return updateCounts;
	}
}
