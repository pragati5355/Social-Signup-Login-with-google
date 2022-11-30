package com.onboarding.web.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserModel {
	
	private String profileImage;

	private String firstName;

	private String lastName;
	
	private String password;

	private String email;

	private String mobileNo;

	private String countryCode;

	private String countryName;


}
