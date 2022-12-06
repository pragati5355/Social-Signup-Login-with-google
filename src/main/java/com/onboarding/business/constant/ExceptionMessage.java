package com.onboarding.business.constant;

public class ExceptionMessage {
	
	private ExceptionMessage() {
		throw new IllegalStateException("Constant class.can't instatiate");
	}

	// Common
	public static final String INTERNAL_SERVER_ERROR = "internal.server.error";

	// User
	public static final String USER_NOT_FOUND = "user.not.found";
	public static final String USER_ALREADY_PRESENT_WITH_DIFFERENT_ACCOUNT = "user.present.with.different.acc";


	public static final String USER_ALREADY_EXIST = "user.already.present";
	
	public static final String GOOGLE_ACCESS_TOKEN_NOT_VALID   = "google.access.token.not.valid";
	
	public static final String ACCESS_TOKEN_NOT_VALID = "access.token.not.valid";


}
