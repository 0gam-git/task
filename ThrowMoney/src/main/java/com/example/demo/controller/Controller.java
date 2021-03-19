package com.example.demo.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.ThrowMoney;
import com.example.demo.model.request.ThrowMoneyRequest;
import com.example.demo.model.response.ReceiptModel;
import com.example.demo.model.response.ThrowMoneyStatusModel;
import com.example.demo.model.response.TokenModel;
import com.example.demo.service.CommonService;
import com.example.demo.service.ReceiptService;
import com.example.demo.service.ThrowMoneyService;
import com.example.demo.util.RedisKeyUtil;
import com.example.demo.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value = "/v1/payment")
@RestController
public class Controller {

	private ReceiptService receiptService;
	private ThrowMoneyService throwMoneyService;
	private CommonService commonService;

	public Controller(ReceiptService receiptService, ThrowMoneyService throwingMoneyService,
			CommonService commonService) {
		this.receiptService = receiptService;
		this.throwMoneyService = throwingMoneyService;
		this.commonService = commonService;
	}

	@PostMapping(value = "/remittance/throwing-money")
	public TokenModel giveMoney(@RequestHeader("X-USER-ID") int userId, @RequestHeader("X-ROOM-ID") String roomId,
			@Validated @RequestBody ThrowMoneyRequest requestModel) {

		log.debug("## userId : {}, roomId : {}, amountPaid : {}, headCount : {}", userId, roomId,
				requestModel.getAmountPaid(), requestModel.getHeadCount());

		int amountPaid = requestModel.getAmountPaid();
		int headCount = requestModel.getHeadCount();

		String token = StringUtil.getEncodeString(RandomStringUtils.random(3));
		roomId = StringUtil.getEncodeString(roomId);

		String lookupKey = RedisKeyUtil.getLookupKey(roomId, token);
		String userIdListKey = RedisKeyUtil.getRestrictedUserListKey(roomId, token);
		String receiptKey = RedisKeyUtil.getReceiptKey(roomId, token);

		throwMoneyService.addThrowingMoney(userId, roomId, amountPaid, headCount, lookupKey);
		commonService.addExpireDays(lookupKey);

		receiptService.addRestrictedUser(userIdListKey, userId);
		commonService.addExpireMinutes(userIdListKey);

		receiptService.distributeRandomMoney(receiptKey, amountPaid, headCount);
		commonService.addExpireMinutes(receiptKey);

		return TokenModel.builder().token(token).build();
	}

	@GetMapping(value = "/remittance/throwing-money/receipt/{token}")
	public ReceiptModel getMoney(@RequestHeader("X-USER-ID") int userId, @RequestHeader("X-ROOM-ID") String roomId,
			@NotEmpty @PathVariable String token) {

		log.debug("## userId : {}, roomId : {}, token : {}", userId, roomId, token);
		roomId = StringUtil.getEncodeString(roomId);

		String receiptKey = RedisKeyUtil.getReceiptKey(roomId, token);
		String userIdListKey = RedisKeyUtil.getRestrictedUserListKey(roomId, token);

		List<Integer> restrictionUserIdList = receiptService.getRestrictedUserList(userIdListKey);

		if (restrictionUserIdList.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please check the token or chat room ID.");
		}
		if (restrictionUserIdList.contains(userId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please check the user ID.");
		}
		if (commonService.isExpire(receiptKey, TimeUnit.MINUTES) < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to receive.");
		}

		int randomMoney = receiptService.pickRandomMoney(receiptKey);

		throwMoneyService.updateThrowingMoney(userId, roomId, token, randomMoney);

		receiptService.addRestrictedUser(userIdListKey, userId);

		return ReceiptModel.builder().amountReceipt(randomMoney).build();
	}

	@GetMapping(value = "/remittance/throwing-money/{token}")
	public ThrowMoneyStatusModel getThrowMoney(@RequestHeader("X-USER-ID") int userId,
			@RequestHeader("X-ROOM-ID") String roomId, @NotEmpty @PathVariable String token) {

		log.debug("## userId : {}, roomId : {}, token : {}", userId, roomId, token);
		String lookupKey = RedisKeyUtil.getLookupKey(roomId, token);

		ThrowMoney model = throwMoneyService.getThrowingMoney(lookupKey);

		if (commonService.isExpire(lookupKey, TimeUnit.DAYS) < 0 || null == model || model.getCreatedById() != userId) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lookup failed.");
		}

		return ThrowMoneyStatusModel.builder().startDateTime(model.getStartDateTime()).amountPaid(model.getAmountPaid())
				.amountReceipt(model.getAmountReceipt()).receipts(model.getReceipts()).build();
	}

}
