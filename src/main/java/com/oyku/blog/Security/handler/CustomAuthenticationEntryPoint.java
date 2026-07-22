package com.oyku.blog.Security.handler;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyku.blog.exception.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		ErrorResponse error = ErrorResponse.builder().status(HttpStatus.UNAUTHORIZED.value()).error("Unauthorized")
				.messages("Authentication is required to access this resource.").path(request.getRequestURI()).build();

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");

		new ObjectMapper().writeValue(response.getOutputStream(), error);

	}
}