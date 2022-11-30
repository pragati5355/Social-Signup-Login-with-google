package com.onboarding.business.exception;

/**
 * validation error message
 * @author Mindbowser | deepak.kumbhar@mindbowser.com
 * @Created Date: May 30, 2021  8:20:14 PM
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ValidatioErrorResponse {

	private String fieldName;
	private String message;

}
