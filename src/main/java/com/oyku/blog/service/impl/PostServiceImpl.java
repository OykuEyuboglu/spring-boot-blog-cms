package com.oyku.blog.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oyku.blog.dto.request.CreatePostRequestDto;
import com.oyku.blog.dto.response.PostResponseDto;
import com.oyku.blog.entity.Post;
import com.oyku.blog.enums.PostStatus;
import com.oyku.blog.repository.PostRepository;
import com.oyku.blog.service.PostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;

	@Override
	@Transactional
	public PostResponseDto createPost(CreatePostRequestDto createPostRequestDto) {

		Post post = new Post();
		post.setTitle(createPostRequestDto.getTitle());
		post.setSummary(createPostRequestDto.getSummary());
		post.setContent(createPostRequestDto.getContent());
		post.setAuthorName(createPostRequestDto.getAuthorName());
		post.setTags(createPostRequestDto.getTags());
		post.setCategory(createPostRequestDto.getCategory());
		post.setStatus(PostStatus.DRAFT);

		Post savedPost = postRepository.save(post);

		return convertToResponseDto(savedPost);
	}

	
	
	@Override
	@Transactional(readOnly = true)
	public List<PostResponseDto> getAllPosts() {

		return postRepository.findAll().stream().map(this::convertToResponseDto).collect(Collectors.toList());
	}

	
	private PostResponseDto convertToResponseDto(Post post) {

		PostResponseDto responseDto = new PostResponseDto();
		responseDto.setId(post.getId());
		responseDto.setTitle(post.getTitle());
		responseDto.setSummary(post.getSummary());
		responseDto.setContent(post.getContent());
		responseDto.setAuthorName(post.getAuthorName());
		responseDto.setTags(
			    post.getTags() != null ? new ArrayList<>(post.getTags()) : new ArrayList<>()
			);
		responseDto.setCategory(post.getCategory());
		responseDto.setStatus(post.getStatus());
		responseDto.setCreatedAt(post.getCreatedAt());
		responseDto.setUpdatedAt(post.getUpdatedAt());

		return responseDto;
	}
}
