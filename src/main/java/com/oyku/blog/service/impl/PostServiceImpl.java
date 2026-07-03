package com.oyku.blog.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oyku.blog.dto.request.CreatePostRequestDto;
import com.oyku.blog.dto.response.PostResponseDto;
import com.oyku.blog.entity.Post;
import com.oyku.blog.enums.PostStatus;
import com.oyku.blog.mapper.PostMapper;
import com.oyku.blog.repository.PostRepository;
import com.oyku.blog.service.PostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final PostMapper postMapper;

	@Override
	@Transactional
	public PostResponseDto createPost(CreatePostRequestDto createPostRequestDto) {

		Post post = postMapper.toEntity(createPostRequestDto);
		post.setStatus(PostStatus.DRAFT);
		
		Post savedPost = postRepository.save(post);
		return postMapper.toResponseDto(savedPost);
	}

	
	
	@Override
	@Transactional(readOnly = true)
	public List<PostResponseDto> getAllPosts() {

		List<Post> posts = postRepository.findAll();
		
		return postMapper.toResponseDtoList(posts);
	}
	
	
	
	
	
}
