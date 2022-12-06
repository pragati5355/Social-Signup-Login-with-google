package com.onboarding.business.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.onboarding.business.constant.ExceptionMessage;
import com.onboarding.business.constant.GenericConstants;
import com.onboarding.business.exception.CustomException;
import com.onboarding.business.util.CustomDozerHelper;
import com.onboarding.business.util.Mapper;
import com.onboarding.persistence.dao.UserDao;
import com.onboarding.persistence.entity.User;
import com.onboarding.persistence.repository.UserRepository;
import com.onboarding.web.jwt.AuthToken;
import com.onboarding.web.jwt.JwtTokenUtil;
import com.onboarding.web.model.FacebookSignupModel;
import com.onboarding.web.model.LoginModel;
import com.onboarding.web.model.ResponseModel;
import com.onboarding.web.model.SocialLoginModel;
import com.onboarding.web.model.UserModel;
import com.onboarding.web.model.UserResponseModel;

@Service
public class UserServiceImpl implements UserService{
	
	private static final HttpTransport TRANSPORT = new NetHttpTransport();

	private static final JacksonFactory JSON_FACTORY = new JacksonFactory();
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private Mapper mapper2;
	
	private DozerBeanMapper mapper = new DozerBeanMapper();
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;


	@Override
	public User addNewUser(UserModel userModel) {
		User user = mapper2.convert(userModel, User.class);
		return userDao.addUser(user);
		
	}

	@Override
	public Map<String, Object> manualSignUp(UserModel userModel) {
		
		Optional<User> user = userDao.findByEmail(userModel.getEmail());
		Map<String, Object> data = new HashMap<>();

		if (user.isPresent()) {
			throw new CustomException(environment.getProperty(ExceptionMessage.USER_ALREADY_EXIST),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		User saveUser = CustomDozerHelper.mapEntity(userModel, User.class);
		String uniqueID = UUID.randomUUID().toString();
		saveUser.setProvider(GenericConstants.MANUAL_AUTH);
		saveUser.setProviderId(GenericConstants.MANUAL_AUTH + uniqueID);
		saveUser.setPassword(passwordEncoder.encode(userModel.getPassword()));
		
		userRepository.save(saveUser);
		
		String token = jwtTokenUtil.authenticationManualUser(saveUser);
		
		UserResponseModel userResponseModel = CustomDozerHelper.mapEntity(saveUser, UserResponseModel.class);
		
		data.put(GenericConstants.SIGNUP_USER_DATA, userResponseModel);
		data.put(GenericConstants.AUTH_TOKEN, token);
		
		return data;
	}
	
	public AuthToken manualLogin(Authentication authentication) {
		
		String token = jwtTokenUtil.generateToken(authentication);
		return new AuthToken(token);
	}

	@Override
	public Map<String, Object> manualLogin(LoginModel loginModel) {
		
		Map<String, Object> data = new HashMap<>();
		
		Optional<User> userData = userDao.findByEmail(loginModel.getEmail());
		
		if(userData.isPresent()) {
			if(userData.get().getProvider().equals(GenericConstants.MANUAL_AUTH)) {
				final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userData.get().getProviderId(),
						loginModel.getPassword()));
				
				AuthToken authToken = manualLogin(authentication);
				
				UserResponseModel userResponseModel = CustomDozerHelper.mapEntity(userData.get(), UserResponseModel.class);
				data.put(GenericConstants.SIGNUP_USER_DATA, userResponseModel);
				data.put(GenericConstants.AUTH_TOKEN, authToken);
				
			} else if(userData.get().getProviderId().equals(GenericConstants.GOOGLE_AUTH)) {
				
				throw new CustomException(environment.getProperty(ExceptionMessage.USER_ALREADY_PRESENT_WITH_DIFFERENT_ACCOUNT), 
						HttpStatus.FORBIDDEN);
			}
		} else {
			throw new CustomException(environment.getProperty(ExceptionMessage.USER_NOT_FOUND), HttpStatus.NOT_FOUND);

		}
		
		return data;
	}
	
	private Map<String, Object> loginGoogleUser(User user, Map<String, Object> data) {
		
		String token = jwtTokenUtil.authenticationUser(user);
		
		UserResponseModel responseModel = CustomDozerHelper.mapEntity(user, UserResponseModel.class);
		
		data.put(GenericConstants.SIGNUP_USER_DATA, responseModel);
		data.put(GenericConstants.AUTH_TOKEN, token);
		
		return data;
	}
	
	private Map<String, Object> saveGoogleUser(Payload payload, Map<String, Object> data , String googleProviderId) {
		
		User user = new User();
		
		String email = payload.getEmail();
		String pictureUrl = (String) payload.get(GenericConstants.PICTURE);
		String firstName = (String) payload.get(GenericConstants.FIRST_NAME);
		String lastName = (String) payload.get(GenericConstants.LAST_NAME);
		
		user.setEmail(email);
		user.setProfileImage(pictureUrl);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setProvider(GenericConstants.GOOGLE_AUTH);
		user.setProviderId(GenericConstants.GOOGLE_AUTH + googleProviderId);
		userRepository.save(user);
		
		String token = jwtTokenUtil.authenticationUser(user);
		
		UserResponseModel responseModel = CustomDozerHelper.mapEntity(user, UserResponseModel.class);
		
		data.put(GenericConstants.SIGNUP_USER_DATA, responseModel);
		data.put(GenericConstants.AUTH_TOKEN, token);
		
		return data;
	}

	@Override
	public Map<String, Object> googleSignUp(SocialLoginModel socialLoginModel) throws GeneralSecurityException, IOException {
		
		Map<String, Object> data = new HashMap<>();
		
		String clientId1 = environment.getProperty(GenericConstants.GOOGLE_CLIENT_ID_1);
		String clientId2 = environment.getProperty(GenericConstants.GOOGLE_CLIENT_ID_2);
		
		GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(TRANSPORT, JSON_FACTORY)
				.setAudience(Arrays.asList(clientId1,clientId2))
				.build();
	
		System.out.println(socialLoginModel.getGoogleToken());
		
		GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(socialLoginModel.getGoogleToken());
		
		System.out.println("googleIdToken ---> "+googleIdToken);
		if(googleIdToken != null) {
			Payload payload = googleIdToken.getPayload();
			System.out.println("=====p===="+payload);	
			Optional<User> user = userDao
					.findByProviderId(GenericConstants.GOOGLE_AUTH + socialLoginModel.getGoogleAuthId());
			
			if(!user.isPresent()) {
				return saveGoogleUser(payload, data, socialLoginModel.getGoogleAuthId());
			} else {
				return loginGoogleUser(user.get(), data);
			}
			
		}
		else {
			throw new CustomException(environment.getProperty(ExceptionMessage.GOOGLE_ACCESS_TOKEN_NOT_VALID),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private FacebookSignupModel callFacebookAPI(String faceBookUrl) {
		
		HttpResponse<String> response;
		
		try {
			response = Unirest.get(faceBookUrl).asString();
			
			JsonElement element = new JsonParser().parse(response.getBody());
			
			JsonObject jsonObject = element.getAsJsonObject();
			
			Gson gson = new Gson();
			
			return gson.fromJson(jsonObject, FacebookSignupModel.class);
		} catch (UnirestException e) {
			throw new CustomException(environment.getProperty(ExceptionMessage.ACCESS_TOKEN_NOT_VALID), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private Map<String, Object> savefacebookUser(FacebookSignupModel facebookSignupModel, Map<String, Object> data,
			SocialLoginModel socialLoginModel) {
		
		User user = new User();
		
		if(socialLoginModel.getFaceBookEmail() != null) {
			user.setEmail(socialLoginModel.getFaceBookEmail());
		} else {
			user.setEmail(facebookSignupModel.getEmail());
		}
		
		user.setFirstName(facebookSignupModel.getFirstName());
		user.setLastName(facebookSignupModel.getLastName());
		user.setProfileImage(facebookSignupModel.getPicture().getData().getUrl());
		user.setProvider(GenericConstants.FACEBOOK_AUTH);
		user.setProviderId(GenericConstants.FACEBOOK_AUTH + facebookSignupModel.getId());
		
		userRepository.save(user);
		
		String token = jwtTokenUtil.authenticationUser(user);
		
		UserResponseModel responseModel = CustomDozerHelper.mapEntity(user, UserResponseModel.class);
		
		data.put(GenericConstants.SIGNUP_USER_DATA, responseModel);
		data.put(GenericConstants.AUTH_TOKEN, token);
		
		return data;
	
	}

	@Override
	public Map<String, Object> facebookSignup(SocialLoginModel socialLoginModel) {
		
		String faceBookUrl = GenericConstants.FACEBOOK + socialLoginModel.getFacebookAcessToken();
		System.out.println("FaceBook Url --> " + faceBookUrl);
		
		Map<String, Object> data = new HashMap<>();
		
		try {
			FacebookSignupModel facebookSignupModel = callFacebookAPI(faceBookUrl);
			System.out.println("After Call Api faceBook Url --> " + facebookSignupModel);
			
			Optional<User> user;
			if(facebookSignupModel.getEmail() != null && facebookSignupModel.getId() != null) {
				user = userDao.findByProviderId(GenericConstants.FACEBOOK_AUTH + facebookSignupModel.getId());
				
			} else {
				throw new CustomException(environment.getProperty(ExceptionMessage.ACCESS_TOKEN_NOT_VALID), 
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			if(!user.isPresent()) {
				return savefacebookUser(facebookSignupModel, data, socialLoginModel);
			}
			
			String token = jwtTokenUtil.authenticationUser(user.get());
			
			UserResponseModel responseModel = CustomDozerHelper.mapEntity(user.get(), UserResponseModel.class);
			
			data.put(GenericConstants.SIGNUP_USER_DATA, responseModel);
			data.put(GenericConstants.AUTH_TOKEN, token);
			return data;
			
		} catch (Exception e) {
			throw new CustomException(environment.getProperty(ExceptionMessage.INTERNAL_SERVER_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Object socialSignUp(SocialLoginModel socialLoginModel) throws GeneralSecurityException, IOException{
		
		if (null != socialLoginModel.getFacebookAcessToken()) {
			return facebookSignup(socialLoginModel);
		}

		if (null != socialLoginModel.getGoogleToken()) {

			return googleSignUp(socialLoginModel);
		}	
		
		return socialLoginModel;
	}
	
	
}
