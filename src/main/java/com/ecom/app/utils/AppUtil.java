package com.ecom.app.utils;

import org.springframework.http.HttpStatus;

import com.ecom.app.exceptions.EcommApiException;

public class AppUtil {
	public static void validatePageNumberAndSize(int page, int size) {
		if (page < 0) {
			throw new EcommApiException(HttpStatus.BAD_REQUEST, "Page number cannot be less than zero.");
		}
		if (size < 0) {
			throw new EcommApiException(HttpStatus.BAD_REQUEST, "Size number cannot be less than zero.");
		}
		if (size > AppConstant.MAX_PAGE_SIZE) {
			throw new EcommApiException(HttpStatus.BAD_REQUEST,
					"Page size must not be greater than " + AppConstant.MAX_PAGE_SIZE);
		}
	}

}
