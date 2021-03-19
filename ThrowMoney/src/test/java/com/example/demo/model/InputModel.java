package com.example.demo.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은 사용자 아이디] 리스트
 */
@Data
public class InputModel implements Serializable {

	private static final long serialVersionUID = 1002790844899723072L;

	private int createdById;

	private ZonedDateTime startDateTime;

	private int headCount;
	private int amountPaid;
	private int amountReceipt;

	List<Receipt> Receipts = new ArrayList<>();
}
