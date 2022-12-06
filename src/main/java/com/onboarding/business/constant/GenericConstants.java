package com.onboarding.business.constant;

public class GenericConstants {
	

	private GenericConstants() {
		throw new IllegalStateException("Constants class.can't instatiate");
	}

	public static final String ERROR_MESSAGE = "Something Went Wrong";
	
	public static final String FACEBOOK = "https://graph.facebook.com/v4.0/me?fields=email,name,first_name,last_name,picture.type(large),friends&access_token=";
	
	public static final String MANUAL_AUTH = "auth";
	
	public static final String SIGNUP_USER_DATA = "userData";

	public static final String AUTH_TOKEN = "authToken";
	
	public static final String GOOGLE_AUTH = "googleAuth";
	
	public static final String GOOGLE_CLIENT_ID_2 = "google.web.client.id";
	
	public static final String GOOGLE_CLIENT_ID_1 = "google.ios.client.id";

	public static final String FIRST_NAME = "given_name";

	public static final String PICTURE = "picture";

	public static final String LAST_NAME = "family_name";	
	
	public static final String FACEBOOK_AUTH = "faceBookAuth";

}
