package com.onboarding.business.enums;

public enum ErrorCode {
	
	INTERNAL_SERVER_ERROR(500) , BAD_REQUEST(400) , NOT_FOUND(404) ,
	UNAUTHORIZED(401) , TOKEN_EXPIRE(120) , TOKEN_INVALID(121) ;
	
	
	private final int code;

	private ErrorCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	

}
