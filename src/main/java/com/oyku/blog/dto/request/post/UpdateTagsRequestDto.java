package com.oyku.blog.dto.request.post;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTagsRequestDto {
	
	@NotEmpty(message = "Tags cannot be empty.")
	private List<String> tags;

}
