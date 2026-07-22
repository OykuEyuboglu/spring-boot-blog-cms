package com.oyku.blog.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class LoginRequest {

	@Email(message = "Invalid email format.")
	@NotBlank(message = "Email is required.")
	private String email;

	@NotBlank
	private String password;
}
