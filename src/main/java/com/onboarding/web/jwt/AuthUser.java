package com.onboarding.web.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthUser extends User{

	private static final long serialVersionUID = 1L;
	
	private String email;
	
	public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object obj) {
		return false;
	}
	
	 @Override
	  public int hashCode() {
		return 0;
	    /* ... */
	  }

}
