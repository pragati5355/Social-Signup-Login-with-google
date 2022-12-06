package com.onboarding.web.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onboarding.business.constant.SuccessMessage;
import com.onboarding.business.constant.UrlMapping;
import com.onboarding.business.exception.CustomException;
import com.onboarding.business.service.UserService;
import com.onboarding.web.model.LoginModel;
import com.onboarding.web.model.ResponseModel;
import com.onboarding.web.model.SocialLoginModel;
import com.onboarding.web.model.UserModel;


@RestController
public class UserController extends BaseController{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Environment env;
	
	@PostMapping(UrlMapping.USER)
	public ResponseEntity<ResponseModel> addUser(@RequestBody UserModel userModel) throws CustomException {
		ResponseModel response = ResponseModel.getInstance();
		response.setData(userService.addNewUser(userModel));
		response.setMessage(env.getProperty(SuccessMessage.SUCCESS));
		response.setStatusCode(HttpStatus.SC_OK);
		return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);

	}
	
	
	@PostMapping(UrlMapping.MANUAL_SIGNUP)
	public ResponseEntity<ResponseModel> manualSignUp(@RequestBody UserModel userModel) throws CustomException {
		ResponseModel response = ResponseModel.getInstance();
		response.setData(userService.manualSignUp(userModel));
		response.setMessage(env.getProperty(SuccessMessage.SUCCESS));
		response.setStatusCode(HttpStatus.SC_OK);
		return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);

	}
	
	@PostMapping(UrlMapping.MANUAL_LOGIN)
	public ResponseEntity<ResponseModel> manualLogin(@RequestBody LoginModel loginModel) throws CustomException {
		ResponseModel response = ResponseModel.getInstance();
		response.setData(userService.manualLogin(loginModel));
		response.setMessage(env.getProperty(SuccessMessage.SUCCESS));
		response.setStatusCode(HttpStatus.SC_OK);
		return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);

	}
	
	@PostMapping(UrlMapping.SOCIAL_SIGNUP)
	public ResponseEntity<ResponseModel> socialSignUp(@RequestBody SocialLoginModel socialLoginModel) throws CustomException, GeneralSecurityException, IOException {
		ResponseModel response = ResponseModel.getInstance();
		response.setData(userService.socialSignUp(socialLoginModel));
		response.setMessage(env.getProperty(SuccessMessage.SUCCESS));
		response.setStatusCode(HttpStatus.SC_OK);
		return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);

	}
	
	

}
