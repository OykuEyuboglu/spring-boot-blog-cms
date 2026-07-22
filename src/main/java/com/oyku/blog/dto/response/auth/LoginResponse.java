package com.oyku.blog.dto.response.auth;

import java.util.Set;

import com.oyku.blog.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

	private String token;
	private String type;
	private Set<Role> role;
	private String name;
	private String email;
	
}
