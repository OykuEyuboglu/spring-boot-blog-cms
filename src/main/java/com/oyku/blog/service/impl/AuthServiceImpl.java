package com.oyku.blog.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oyku.blog.Security.jwt.JwtService;
import com.oyku.blog.dto.request.auth.LoginRequest;
import com.oyku.blog.dto.request.auth.RegisterRequest;
import com.oyku.blog.dto.response.auth.AuthResponse;
import com.oyku.blog.dto.response.auth.RegisterResponse;
import com.oyku.blog.entity.User;
import com.oyku.blog.enums.Role;
import com.oyku.blog.exception.ConflictException;
import com.oyku.blog.mapper.UserMapper;
import com.oyku.blog.repository.UserRepository;
import com.oyku.blog.service.AuthService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	@Override
	public RegisterResponse register(RegisterRequest request) {

	    if (userRepository.existsByEmail(request.getEmail())) {
	        throw new ConflictException("Email already exists.");
	    }

	    User user = userMapper.toEntity(request);

	    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

	    user.getRole().add(Role.USER);

	    User savedUser = userRepository.save(user);

	    LOGGER.info("Register request called successfully.");

	    return userMapper.toRegisterResponse(savedUser);
	}
	
	@Override
	public AuthResponse login(LoginRequest request) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		UserDetails user = (UserDetails) authentication.getPrincipal();

		String token = jwtService.generateToken(user);

		LOGGER.info("Login request called successfully.");
		
		return AuthResponse.builder().token(token).build();

	}
}