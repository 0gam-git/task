package com.example.demo.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ThrowMoney implements Serializable {

	private static final long serialVersionUID = 560786696649370851L;

	private int createdById;

	private ZonedDateTime startDateTime;

	private int headCount;
	private int amountPaid;
	private int amountReceipt;

	List<Receipt> receipts = new ArrayList<>();
}
