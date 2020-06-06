package com.coupon.kakaopay.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.coupon.kakaopay.model.type.CouponStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString(exclude = "user")
public class UserCoupon implements Serializable {

	private static final long serialVersionUID = 9000619627838512755L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long serial;

	@JsonManagedReference
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "coupon_id", unique = true)
	private Coupon coupon;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated(EnumType.ORDINAL)
	private CouponStatus status;

	@Column
	private LocalDate startDate;

	@Column
	private LocalDate expiryDate;

	public UserCoupon(Coupon coupon, User user, CouponStatus status) {
		this.coupon = coupon;
		this.user = user;
		this.status = status;
	}

}
