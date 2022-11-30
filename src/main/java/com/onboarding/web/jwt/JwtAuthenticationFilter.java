package com.onboarding.web.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.onboarding.business.constant.JwtConstants;

import io.jsonwebtoken.Claims;

public class JwtAuthenticationFilter extends OncePerRequestFilter{

	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String header = request.getHeader(JwtConstants.HEADER_STRING);
		
		if(StringUtils.isNotBlank(header) && header.startsWith(JwtConstants.TOKEN_PREFIX)) {
			
			String authToken = header.replace(JwtConstants.TOKEN_PREFIX, "");
			Claims claims = jwtTokenUtil.getJwtClaims(authToken);
			
			String username = claims.getSubject();
			
			if(jwtTokenUtil.validateToken(claims)) {
				
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
						"", getAuthoritiesFromString(claims));
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				request.setAttribute("data", username);
				
			}
			
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	private Collection<? extends GrantedAuthority> getAuthoritiesFromString(Claims claims) {
		
		return Arrays.stream(claims.get(JwtConstants.AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

}
