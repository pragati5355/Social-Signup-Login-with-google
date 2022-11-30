package com.onboarding.business.util;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.onboarding.business.exception.ValidatioErrorResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_DEFAULT)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ErrorResponse {
	

	private Integer statusCode;

	private Boolean error;

	private Date timestamp;

	private String message;

	private List<ValidatioErrorResponse> validationErros;
	
	public ErrorResponse(String message, Integer errorcode, Boolean error, Date timestamp) {
		super();
		this.message = message;
		this.statusCode = errorcode;
		this.error = error;
		this.timestamp = timestamp;
	}

	
	public ErrorResponse(String message, Integer errorcode, Boolean error, Date timestamp, List<ValidatioErrorResponse> validationErros) {
		super();
		this.message = message;
		this.statusCode = errorcode;
		this.error = error;
		this.timestamp = timestamp;
		this.validationErros = validationErros;
	}


}
