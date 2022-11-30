package com.onboarding.persistence.dao;

import java.util.Optional;

import com.onboarding.persistence.entity.User;

public interface UserDao {
	
	public User addUser(User user);
	
	public Optional<User> findByProviderId(String id);
	
	public Optional<User> findByEmail(String email);

}
