package com.oyku.blog.service;

import com.oyku.blog.dto.request.auth.LoginRequest;
import com.oyku.blog.dto.request.auth.RegisterRequest;
import com.oyku.blog.dto.response.auth.AuthResponse;
import com.oyku.blog.dto.response.auth.RegisterResponse;

public interface AuthService {

	RegisterResponse register(RegisterRequest request);
	
	AuthResponse login(LoginRequest request);
}
