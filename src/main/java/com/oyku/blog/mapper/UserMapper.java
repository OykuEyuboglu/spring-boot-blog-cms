package com.oyku.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.oyku.blog.dto.request.auth.RegisterRequest;
import com.oyku.blog.dto.response.auth.RegisterResponse;
import com.oyku.blog.dto.response.auth.UserResponse;
import com.oyku.blog.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "passwordHash", ignore = true)
	User toEntity(RegisterRequest request);

	RegisterResponse toRegisterResponse(User user);

	UserResponse toUserResponse(User user);

}
