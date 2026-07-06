package com.oyku.blog.dto.request.post;

import com.oyku.blog.enums.PostStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchPostRequest {
	
	private PostStatus status;
	private String categoryId;
	private String keyword;
	private String author;

}
