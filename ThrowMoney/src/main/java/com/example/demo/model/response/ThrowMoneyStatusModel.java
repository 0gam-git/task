package com.example.demo.model.response;

import java.time.ZonedDateTime;
import java.util.List;

import com.example.demo.model.Receipt;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ThrowMoneyStatusModel {

	private ZonedDateTime startDateTime;

	private int amountPaid;
	private int amountReceipt;

	List<Receipt> receipts;

	@Builder
	public ThrowMoneyStatusModel(ZonedDateTime startDateTime, int amountPaid, int amountReceipt,
			List<Receipt> receipts) {
		this.startDateTime = startDateTime;
		this.amountPaid = amountPaid;
		this.amountReceipt = amountReceipt;
		this.receipts = receipts;
	}

}
