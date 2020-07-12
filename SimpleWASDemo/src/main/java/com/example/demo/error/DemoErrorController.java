package com.example.demo.error;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoErrorController implements ErrorController {

	@Value("${demo.error.forbidden}")
	private String forbiddenPage;

	@Value("${demo.error.not_found}")
	private String notFoundPage;

	@Value("${demo.error.internal_server_error}")
	private String internalServerErrorPage;

	@Override
	public String getErrorPath() {
		return "/error";
	}

	@RequestMapping(value = "/error")
	public String handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());
			if (statusCode == HttpStatus.FORBIDDEN.value()) {
				return forbiddenPage;

			} else if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return notFoundPage;

			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				return internalServerErrorPage;
			}
		}

		return "error";
	}

}
