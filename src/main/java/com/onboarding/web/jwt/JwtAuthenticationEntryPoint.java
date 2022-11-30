package com.onboarding.web.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

	Logger logger = LogManager.getLogger();
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		logger.info("JwtAuthenticationEntryPoint commence() method {}  ", authException.getMessage());

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "" + "Please enter valid password");
		
	}

}
