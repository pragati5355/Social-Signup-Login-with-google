package com.onboarding.business.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SucessResponse<T> {
	
	private String message;
	private Integer code;
	private T data;
	private Boolean error;

}
