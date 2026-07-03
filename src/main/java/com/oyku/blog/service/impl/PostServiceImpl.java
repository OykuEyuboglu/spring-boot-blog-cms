package com.oyku.blog.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oyku.blog.dto.request.CreatePostRequestDto;
import com.oyku.blog.dto.request.UpdatePostRequestDto;
import com.oyku.blog.dto.response.PostResponseDto;
import com.oyku.blog.entity.Post;
import com.oyku.blog.enums.PostStatus;
import com.oyku.blog.exception.ResourceNotFoundException;
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



	@Override
	@Transactional(readOnly = true)
	public PostResponseDto getPostById(String id) {

	    Post post = postRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Post bulunamadı."));

	    return postMapper.toResponseDto(post);
	}



	@Override
	@Transactional
	public PostResponseDto updatePost(String id, UpdatePostRequestDto updatePostRequestDto) {

		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post bulunamadı."));

		postMapper.updateEntityFromDto(updatePostRequestDto, post);
		
		Post updatedPost = postRepository.save(post);
		
		return postMapper.toResponseDto(updatedPost);
	}



	@Override
	@Transactional
	public void deletePost(String id) {

		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post bulunamadı"));
		
		postRepository.delete(post);
	}



	@Override
	@Transactional
	public PostResponseDto publishPost(String id) {

		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post bulunamadı"));

		post.setStatus(PostStatus.PUBLISHED);
		
		Post publishedPost = postRepository.save(post);
		return postMapper.toResponseDto(publishedPost);
	}
	
	
	@Override
	@Transactional
	public PostResponseDto draftPost(String id) {
		
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post bulunamadı"));
		
		post.setStatus(PostStatus.DRAFT);
		
		Post draftPost = postRepository.save(post);
		return postMapper.toResponseDto(draftPost);	
	}
	
}
