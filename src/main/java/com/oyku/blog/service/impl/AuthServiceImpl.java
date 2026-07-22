package com.oyku.blog.service.impl;

import org.springframework.stereotype.Service;

import com.oyku.blog.dto.request.auth.LoginRequest;
import com.oyku.blog.dto.request.auth.RegisterRequest;
import com.oyku.blog.dto.response.auth.AuthResponse;
import com.oyku.blog.dto.response.auth.RegisterResponse;
import com.oyku.blog.mapper.UserMapper;
import com.oyku.blog.repository.UserRepository;
import com.oyku.blog.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	
	@Override
	public RegisterResponse register(RegisterRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthResponse login(LoginRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
