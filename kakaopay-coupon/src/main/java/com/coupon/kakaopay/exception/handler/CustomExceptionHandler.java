package com.coupon.kakaopay.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.coupon.kakaopay.exception.BadRequestException;
import com.coupon.kakaopay.exception.InvalidNumberSizeException;
import com.coupon.kakaopay.model.response.ResponseModel;
import com.coupon.kakaopay.util.DateUtil;
import com.coupon.kakaopay.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	@ResponseBody
	public ResponseModel handleBadRequestException(WebRequest req, BadRequestException ex) {
		ResponseModel errorModel = new ResponseModel();
		errorModel.setTimestamp(DateUtil.getTimestamp());
		errorModel.setPath(ResponseUtil.getUri(req));
		errorModel.setStatus(HttpStatus.BAD_REQUEST.hashCode());
		errorModel.setMessage(ex.getLocalizedMessage());
		return errorModel;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidNumberSizeException.class)
	@ResponseBody
	public ResponseModel handleInvalidNumberSizeException(WebRequest req, InvalidNumberSizeException ex) {
		ResponseModel errorModel = new ResponseModel();
		errorModel.setTimestamp(DateUtil.getTimestamp());
		errorModel.setPath(ResponseUtil.getUri(req));
		errorModel.setStatus(HttpStatus.BAD_REQUEST.hashCode());
		errorModel.setMessage(ex.getLocalizedMessage());
		return errorModel;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseModel handleBadRequest(WebRequest req, Exception ex) {
		log.error("BAD_REQUEST - {}", ex.getMessage());
		ResponseModel errorModel = new ResponseModel();
		errorModel.setTimestamp(DateUtil.getTimestamp());
		errorModel.setPath(ResponseUtil.getUri(req));
		errorModel.setStatus(HttpStatus.BAD_REQUEST.hashCode());
		errorModel.setMessage(ex.getLocalizedMessage());
		return errorModel;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ResponseModel handleNotFound(WebRequest req, Exception ex) {
		ResponseModel errorModel = new ResponseModel();
		errorModel.setTimestamp(DateUtil.getTimestamp());
		errorModel.setPath(ResponseUtil.getUri(req));
		errorModel.setStatus(HttpStatus.NOT_FOUND.hashCode());
		errorModel.setMessage(ex.getLocalizedMessage());
		return errorModel;
	}

	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ResponseBody
	public ResponseModel handleNotJsonRequest(WebRequest req, Exception ex) {
		log.error("UNSUPPORTED_MEDIA_TYPE - {}", ex.getMessage());
		ResponseModel errorModel = new ResponseModel();
		errorModel.setTimestamp(DateUtil.getTimestamp());
		errorModel.setPath(ResponseUtil.getUri(req));
		errorModel.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.hashCode());
		errorModel.setMessage(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase());
		return errorModel;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ResponseModel handleInternalServerError(WebRequest req, Exception ex) {
		log.error("INTERNAL_SERVER_ERROR - {}", ex.getMessage());
		ResponseModel errorModel = new ResponseModel();
		errorModel.setTimestamp(DateUtil.getTimestamp());
		errorModel.setPath(ResponseUtil.getUri(req));
		errorModel.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.hashCode());
		errorModel.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		return errorModel;
	}

}
