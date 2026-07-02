package com.oyku.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oyku.blog.dto.request.CreatePostRequestDto;
import com.oyku.blog.dto.response.PostResponseDto;
import com.oyku.blog.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
}