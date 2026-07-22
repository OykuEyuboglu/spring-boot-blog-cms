package com.oyku.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oyku.blog.dto.request.auth.LoginRequest;
import com.oyku.blog.dto.request.auth.RegisterRequest;
import com.oyku.blog.dto.response.auth.AuthResponse;
import com.oyku.blog.dto.response.auth.RegisterResponse;
import com.oyku.blog.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
		
		LOGGER.info("register endpoint called");
		RegisterResponse response = authService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

		LOGGER.info("Login endpoint called");
		AuthResponse response = authService.login(request);
		return ResponseEntity.ok(response);
	}
}
