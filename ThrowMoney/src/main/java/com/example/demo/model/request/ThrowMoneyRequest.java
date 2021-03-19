package com.example.demo.model.request;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThrowMoneyRequest {

	@Min(1)
	int amountPaid;

	@Min(1)
	int headCount;

}
