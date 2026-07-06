package com.oyku.blog.service;

import java.util.List;

import com.oyku.blog.dto.request.comment.CreateCommentRequestDto;
import com.oyku.blog.dto.request.post.CreatePostRequestDto;
import com.oyku.blog.dto.request.post.UpdatePostRequestDto;
import com.oyku.blog.dto.response.comment.CommentResponseDto;
import com.oyku.blog.dto.response.post.PostResponseDto;
import com.oyku.blog.entity.Post;

public interface PostService {
	PostResponseDto createPost(CreatePostRequestDto createPostRequestDto);

	List<PostResponseDto> getAllPosts();
	
	PostResponseDto getPostById(String id);
	
	PostResponseDto updatePost(String id, UpdatePostRequestDto updatePostRequestDto);
	
	void deletePost(String id);
	
	PostResponseDto publishPost(String id);
	
	PostResponseDto draftPost(String id);
	
	Post findPostbyIdOrThrow(String id);

	PostResponseDto addComment(String id, CreateCommentRequestDto request);
	
	List<CommentResponseDto> getCommentsByPostId(String id);
}
