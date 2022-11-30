package com.onboarding.web.jwt;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.onboarding.persistence.dao.UserDao;
import com.onboarding.persistence.entity.User;

@Service("CustomUserDetailsService")
public class CustomerUserDetailService implements UserDetailsService{
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		
        Optional<User> user = userDao.findByProviderId(id);
        
        if(user != null) {
        	return new AuthUser(user.get().getProviderId(), user.get().getPassword(), getAuthority());
        } else {
        	throw new UsernameNotFoundException("Bad credentials");
        }
	}
	
	private List<SimpleGrantedAuthority> getAuthority() {

		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}
	
	public String authenticateUser(User user) {

		AuthUser authUser = new AuthUser(user.getProviderId(), user.getPassword(), getAuthority());

		Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, "",
				authUser.getAuthorities());

		final String token = jwtTokenUtil.generateToken(authentication);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return token;
	}
	

}
