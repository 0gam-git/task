package com.example.demo.model.request;

import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ThrowMoneyRequest {

	@Range(min = 1, max = 10000000, message = "The payment amount is between {min} and {max}.")
	int amountPaid;

	@Range(min = 1, max = 1000, message = "The number of people is from {min} to {max}.")
	int headCount;

}
