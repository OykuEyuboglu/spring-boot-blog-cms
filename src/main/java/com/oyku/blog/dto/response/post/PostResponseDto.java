package com.oyku.blog.dto.response.post;

import java.time.LocalDateTime;
import java.util.List;

import com.oyku.blog.dto.response.comment.CommentResponseDto;
import com.oyku.blog.enums.PostStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {

	private String id;
	private String slug;
	private String title;
	private String summary;
	private String content;
	private String authorName;
	private List<String> tags;
	private PostStatus status;
	private Long categoryId;
	private List<CommentResponseDto> comments;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}