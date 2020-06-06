package com.coupon.kakaopay.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coupon.kakaopay.model.dto.User;
import com.coupon.kakaopay.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Transactional(readOnly = true)
	public Optional<User> getBySerial(Long serial) {
		return userRepository.findById(serial);
	}

	@Transactional(readOnly = true)
	public Optional<User> getByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Transactional
	public User save(User user) {
		return userRepository.save(user);
	}

	@Transactional
	public void deleteBySerial(Long serial) {
		log.info("## UserService - delete : {}", serial);
		userRepository.deleteById(serial);
	}

}
