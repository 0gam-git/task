package com.example.demo.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenModel {

	private String token;

	@Builder
	public TokenModel(String token) {
		this.token = token;
	}
}
