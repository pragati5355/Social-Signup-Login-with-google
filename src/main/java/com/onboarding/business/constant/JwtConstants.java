package com.onboarding.business.constant;

public class JwtConstants {
	
	private JwtConstants() {
		throw new IllegalStateException("JWT Constants class.can't initiate");
	}

	public static final String TOKEN_PREFIX = "Bearer";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGNING_KEY = "MB_TSK_ASS";

	public static final String UUID = "uuid";
	public static final String NAME = "name";
	public static final String AUTHORITIES_KEY = "role";
}
