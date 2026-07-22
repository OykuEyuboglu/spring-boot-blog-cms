package com.oyku.blog.dto.response.auth;

import java.time.LocalDateTime;
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
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

	private Long id;
	private String name;
	private String email;	
	private Set<Role> role;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
