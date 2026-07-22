package com.oyku.blog.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	@NotBlank
	@Size(max = 100)
	private String name;

	@Email(message = "Invalid email format.")
	@NotBlank(message = "Email is required.")
	@Size(max = 100)
	private String email;

	@NotBlank
	@Size(min = 8, max = 50)
	private String password;
}
