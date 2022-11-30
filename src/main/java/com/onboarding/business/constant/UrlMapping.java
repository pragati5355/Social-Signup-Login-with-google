package com.onboarding.business.constant;

public class UrlMapping {
	
	private UrlMapping() {
		throw new IllegalStateException("Constant class.can't instatiate");
	}
	
	public static final String BASE_URL = "api";
	
	public static final String USER = "user";
	
	public static final String MANUAL_SIGNUP = "manual/signup";
	
	public static final String SOCIAL_SIGNUP = "social/signup";
	
	public static final String MANUAL_LOGIN = "manual/login";
	
	public static final String EXCEPTION_MESSAGE = "Could Not found";

}
