package com.oyku.blog.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.oyku.blog.enums.Category;
import com.oyku.blog.enums.PostStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {

	private String id;
	private String title;
	private String summary;
	private String content;
	private String authorName;
	private List<String> tags;
	private PostStatus status;
	private Category category;
	private List<CommentResponseDto> comments;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}