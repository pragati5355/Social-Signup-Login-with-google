package com.onboarding.web.jwt;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.onboarding.business.constant.JwtConstants;
import com.onboarding.business.enums.ErrorCode;
import com.onboarding.business.exception.CustomException;
import com.onboarding.persistence.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtTokenUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
	
	public Claims getJwtClaims(String jwtToken) {
		
		Claims claims = null;
		
		try {
			claims = Jwts.parser().setSigningKey(JwtConstants.SIGNING_KEY).parseClaimsJws(jwtToken).getBody();
		} catch (ExpiredJwtException e) {
			LOGGER.info("--- Token is Expired --- ");
			throw new CustomException("Token is Expired", ErrorCode.TOKEN_EXPIRE);
		} catch (SignatureException | MalformedJwtException e) {
			LOGGER.info("--- Token is Invalid --- ");
			throw new CustomException("Invalid Token", ErrorCode.TOKEN_INVALID);
		} catch (Exception e) {
			LOGGER.info("--- Internal Server Error while parsing token --- ");
			throw new CustomException("Internal Error while parsing token", ErrorCode.INTERNAL_SERVER_ERROR);
		}
		
		return claims;
		
	}
	
	public String generateToken(Authentication authentication) {
		
		LOGGER.info("In generate Token");
		
		AuthUser authUser = (AuthUser) authentication.getPrincipal();
		LOGGER.info(String.format("uuid --> ", authUser));
		
		final String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		return Jwts.builder().claim(JwtConstants.UUID, authUser)
				.claim(JwtConstants.AUTHORITIES_KEY, authorities).setSubject(authentication.getName())
				.setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(getTokenValidity()))
				.signWith(SignatureAlgorithm.HS512, JwtConstants.SIGNING_KEY).compact();
		
	}
	
	public boolean validateToken(Claims claims) {
		return !claims.getExpiration().before(new Date());
	}

	private long getTokenValidity() {
		
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DATE, 1);
	    return calendar.getTimeInMillis();
	}
	
	public Claims parseJwtToken(String token) {
		return Jwts.parser().setSigningKey(JwtConstants.SIGNING_KEY).parseClaimsJws(token).getBody();
	}
	
	private String getSubjectFromJwtToken(String token) {
		return parseJwtToken(token).getSubject();
	}
	
	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}
	
	public String authenticationUser(User user) {
		
		AuthUser authUser = new AuthUser(user.getProviderId(), "", getAuthority());
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, "" , 
				authUser.getAuthorities());
		
		final String token = generateToken(authentication);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return token;
		
	}
	
	public String authenticationManualUser(User user) {
		
		AuthUser authUser = new AuthUser(user.getProviderId(), user.getPassword(), getAuthority());
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, "", 
				authUser.getAuthorities());
		

		final String token = generateToken(authentication);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return token;
	}
	
	public String createTokenForVerification(User user) {

		AuthUser authUser = new AuthUser(user.getEmail(), user.getPassword(), getAuthority());

		Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, "",
				authUser.getAuthorities());
		
		final String token = generateToken(authentication);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return token;
	}

	public String generateToken(String email) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, email);
	}
	
	public String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.signWith(SignatureAlgorithm.HS512, JwtConstants.SIGNING_KEY).compact();
		
	}
	

}

