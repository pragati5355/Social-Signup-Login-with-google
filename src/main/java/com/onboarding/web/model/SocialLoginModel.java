package com.onboarding.web.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class SocialLoginModel extends BaseModel{
	
	private static final long serialVersionUID = 1L;
	
	private String googleAuthId;
	private String googleToken;

	
	

}
