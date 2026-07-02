package com.oyku.blog.dto.request;

import java.util.List;

import com.oyku.blog.enums.Category;
import com.oyku.blog.enums.PostStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequestDto {
	
	private String title;
	private String summary;
	private String content;
	private String authorName;
	private PostStatus status;
    private List<String> tags;
    private Category category;
	
}
