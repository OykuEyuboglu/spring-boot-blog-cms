package com.oyku.blog.service;

import java.util.List;

import com.oyku.blog.dto.request.CreatePostRequestDto;
import com.oyku.blog.dto.response.PostResponseDto;

public interface PostService {
	PostResponseDto createPost(CreatePostRequestDto createPostRequestDto);

	List<PostResponseDto> getAllPosts();
}
