package com.onboarding.web.model;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ResponseModel {

	private String error;
	private Object data;
	private Integer statusCode;
	private String message;
	
	public static ResponseModel getInstance()
	{
		ResponseModel response = new ResponseModel();
		response.setStatusCode(HttpStatus.OK.value());
		return response;
	}
}
