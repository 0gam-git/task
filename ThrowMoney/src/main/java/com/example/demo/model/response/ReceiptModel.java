package com.example.demo.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReceiptModel {

	private int amountReceipt;

	@Builder
	public ReceiptModel(int amountReceipt) {
		this.amountReceipt = amountReceipt;
	}
}
