package com.onboarding.web.model;

import com.google.gson.annotations.SerializedName;

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
public class FacebookSignupModel {
	
	private String email;
	private String name;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("last_name")
	private String lastName;
	
	private FaceBookPicture picture;
	
	private String id;


}
