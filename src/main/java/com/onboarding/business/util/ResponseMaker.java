package com.onboarding.business.util;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.onboarding.business.exception.ValidatioErrorResponse;


@Component
public class ResponseMaker {

	public <T> ResponseEntity<SucessResponse<T>> successResponse(String message, T data) {

		SucessResponse<T> response = new SucessResponse<>(message, HttpStatus.OK.value(), data, false);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	public ResponseEntity<ErrorResponse> errorResponse(String message, Integer errorCode) {

		ErrorResponse response = new ErrorResponse(message, errorCode, true, new Date());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}	

	public ResponseEntity<Object> validationErrorResponse(String message, Integer errorCode,
			List<ValidatioErrorResponse> validationErros) {

		ErrorResponse response = new ErrorResponse(message, errorCode, true, new Date(), validationErros);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


}
