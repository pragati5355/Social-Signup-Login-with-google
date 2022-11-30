package com.onboarding.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onboarding.persistence.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findUserByProviderIdAndDeleted(String id, boolean b);
	
	Optional<User> findByEmailAndDeleted(String email, boolean b);
}
