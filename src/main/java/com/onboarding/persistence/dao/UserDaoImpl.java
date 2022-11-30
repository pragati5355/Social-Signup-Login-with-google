package com.onboarding.persistence.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.onboarding.business.constant.GenericConstants;
import com.onboarding.business.enums.ErrorCode;
import com.onboarding.business.exception.CustomException;
import com.onboarding.persistence.entity.User;
import com.onboarding.persistence.repository.UserRepository;

@Repository
public class UserDaoImpl implements UserDao{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User addUser(User user) {
		try {
			return userRepository.save(user);
			
		}catch(Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}

	@Override
	public Optional<User> findByProviderId(String id) {
		
		try {
			return userRepository.findUserByProviderIdAndDeleted(id, false);
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), ErrorCode.NOT_FOUND);
		}
	}

	@Override
	public Optional<User> findByEmail(String email) {
	
		try {
			
			return userRepository.findByEmailAndDeleted(email, false);
		} catch(Exception e) {
			throw new CustomException(e.getMessage(), ErrorCode.NOT_FOUND);
		}
		
	}

}
