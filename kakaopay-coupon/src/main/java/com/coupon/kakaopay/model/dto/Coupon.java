package com.coupon.kakaopay.model.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Coupon implements Serializable {

	private static final long serialVersionUID = 1136558622437527245L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long serial;

	@NonNull
	@Column(length = 80, unique = true)
	private String code;

	@JsonBackReference
	@OneToOne
	private UserCoupon userCoupon;

}