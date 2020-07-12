package com.coupon.kakaopay.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User implements Serializable {

	private static final long serialVersionUID = -8907109706799352767L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long serial;

	@NonNull
	@Email(message = "The string has to be a well-formed email address.")
	@Column(unique = true)
	private String email;

	@NonNull
	@Transient
	private String password;

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<UserCoupon> userCoupons;

	public User(@NonNull String email, @NonNull String password) {
		this.email = email;
		this.password = password;
		this.userCoupons = new ArrayList<UserCoupon>();
	}

}
