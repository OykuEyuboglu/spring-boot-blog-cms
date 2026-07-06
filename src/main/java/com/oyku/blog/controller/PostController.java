package com.oyku.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oyku.blog.dto.request.comment.CreateCommentRequestDto;
import com.oyku.blog.dto.request.post.CreatePostRequestDto;
import com.oyku.blog.dto.request.post.UpdatePostRequestDto;
import com.oyku.blog.dto.response.comment.CommentResponseDto;
import com.oyku.blog.dto.response.post.PostResponseDto;
import com.oyku.blog.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody CreatePostRequestDto createPostRequestDto) {

		PostResponseDto createdPost = postService.createPost(createPostRequestDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
	}

	@GetMapping
	public ResponseEntity<List<PostResponseDto>> getAllPosts() {

		List<PostResponseDto> posts = postService.getAllPosts();

		return ResponseEntity.ok(posts);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponseDto> getPostById(@PathVariable String id) {

		PostResponseDto post = postService.getPostById(id);

		return ResponseEntity.ok(post);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<PostResponseDto> updatePost(@PathVariable String id,
			@Valid @RequestBody UpdatePostRequestDto updatePostRequestDto) {

		PostResponseDto updatedPost = postService.updatePost(id, updatePostRequestDto);
		return ResponseEntity.ok(updatedPost);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable String id) {
		postService.deletePost(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/publish")
	public ResponseEntity<PostResponseDto> publishPost(@PathVariable String id) {
		PostResponseDto publishedPost = postService.publishPost(id);
		return ResponseEntity.ok(publishedPost);
	}

	@PatchMapping("/{id}/draft")
	public ResponseEntity<PostResponseDto> draftPost(@PathVariable String id) {
		PostResponseDto draftedPost = postService.publishPost(id);
		return ResponseEntity.ok(draftedPost);
	}
	
	
	
	
	@PostMapping("/{id}/comments")
	public ResponseEntity<String> addComment(@PathVariable String id, @RequestBody @Valid CreateCommentRequestDto request) {
		
		postService.addComment(id, request);
		return ResponseEntity.ok("Comment added successfully");
	}
	
	@GetMapping("/{id}/comments")
	public ResponseEntity<List<CommentResponseDto>>getCommentsById(@PathVariable String id){
				
		List<CommentResponseDto> comments = postService.getCommentsByPostId(id);

		return ResponseEntity.ok(comments);
	}
	

}