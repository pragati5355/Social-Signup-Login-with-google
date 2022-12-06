package com.onboarding.business.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import com.onboarding.persistence.entity.User;
import com.onboarding.web.model.LoginModel;
import com.onboarding.web.model.SocialLoginModel;
import com.onboarding.web.model.UserModel;

public interface UserService {
	
	public User addNewUser(UserModel userModel);
	
	public Map<String, Object> manualSignUp(UserModel userModel);
	
	public Map<String, Object> manualLogin(LoginModel loginModel);
	
	public Map<String, Object> googleSignUp(SocialLoginModel socialLoginModel) throws GeneralSecurityException, IOException;

	public Map<String, Object> facebookSignup(SocialLoginModel socialLoginModel);
	
	public Object socialSignUp(SocialLoginModel socialLoginModel)throws GeneralSecurityException, IOException;
}
