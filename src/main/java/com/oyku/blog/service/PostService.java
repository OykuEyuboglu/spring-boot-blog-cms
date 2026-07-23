package com.oyku.blog.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.oyku.blog.dto.request.comment.CreateCommentRequestDto;
import com.oyku.blog.dto.request.post.CreatePostRequestDto;
import com.oyku.blog.dto.request.post.RemoveTagsRequestDto;
import com.oyku.blog.dto.request.post.SearchPostRequest;
import com.oyku.blog.dto.request.post.UpdatePostRequestDto;
import com.oyku.blog.dto.request.post.UpdateTagsRequestDto;
import com.oyku.blog.dto.response.PageResponse;
import com.oyku.blog.dto.response.comment.CommentResponseDto;
import com.oyku.blog.dto.response.post.PostResponseDto;
import com.oyku.blog.dto.response.statistics.AuthorStatisticsResponseDto;
import com.oyku.blog.dto.response.statistics.CategoryStatisticsResponseDto;
import com.oyku.blog.dto.response.statistics.StatusStatisticsResponseDto;
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
	
	List<PostResponseDto> searchPosts(SearchPostRequest request);
	
	PageResponse<PostResponseDto> getPosts(int page, int size);

	List<StatusStatisticsResponseDto> getStatusStatistics();
	
	List<AuthorStatisticsResponseDto> getAuthorStatistics();

	List<CategoryStatisticsResponseDto> getCategoryStatistics();

	List<PostResponseDto> getLatestPosts(int limit);

	PostResponseDto addTags(String id, UpdateTagsRequestDto request);
	
	PostResponseDto removeTag(String id, RemoveTagsRequestDto request);
}
