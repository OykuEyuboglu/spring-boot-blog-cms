package com.oyku.blog.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.oyku.blog.entity.Category;
import com.oyku.blog.entity.Post;
import com.oyku.blog.entity.User;
import com.oyku.blog.enums.PostStatus;
import com.oyku.blog.enums.Role;
import com.oyku.blog.exception.ForbiddenException;
import com.oyku.blog.exception.ResourceNotFoundException;
import com.oyku.blog.mapper.CommentMapperImpl;
import com.oyku.blog.mapper.PostMapper;
import com.oyku.blog.model.Comment;
import com.oyku.blog.repository.CategoryRepository;
import com.oyku.blog.repository.PostRepository;
import com.oyku.blog.repository.UserRepository;
import com.oyku.blog.service.PostService;
import com.oyku.blog.specification.PostSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final CommentMapperImpl commentMapperImpl;
	private final PostRepository postRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final PostMapper postMapper;
	private final SlugService slugService;

	@Override
	@Transactional
	@CacheEvict(value = "posts", allEntries = true)
	public PostResponseDto createPost(CreatePostRequestDto createPostRequestDto) {

		Post post = postMapper.toEntity(createPostRequestDto);

		Category category = categoryRepository.findById(createPostRequestDto.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category does not exist."));

		post.setCategory(category);
		post.setStatus(PostStatus.DRAFT);

		post.setSlug(slugService.generateSlug(post.getTitle()));

		User currentUser = getCurrentUser();
		post.setUser(currentUser);

		Post savedPost = postRepository.save(post);
		return postMapper.toResponseDto(savedPost);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable("posts")
	public List<PostResponseDto> getAllPosts() {

		List<Post> posts = postRepository.findAll();

		return postMapper.toResponseDtoList(posts);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "post", key = "#id")
	public PostResponseDto getPostById(String id) {

		Post post = findPostbyIdOrThrow(id);

		return postMapper.toResponseDto(post);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommentResponseDto> getCommentsByPostId(String id) {

		Post post = findPostbyIdOrThrow(id);

		return commentMapperImpl.toResponseDtos(post.getComments());
	}

	@Override
	@Transactional
	@CachePut(value = "post", key = "#id")
	public PostResponseDto updatePost(String id, UpdatePostRequestDto request) {
				Post post = findPostbyIdOrThrow(id);

	validatePostOwner(post);
		

		postMapper.updateEntityFromDto(request, post);

		if (request.getTitle() != null && !request.getTitle().isBlank()) {
			post.setSlug(slugService.generateSlug(request.getTitle()));
		}
		Post updatedPost = postRepository.save(post);

		return postMapper.toResponseDto(updatedPost);
	}

	@Override
	@Transactional
	public PostResponseDto addTags(String id, UpdateTagsRequestDto request) {

		Post post = findPostbyIdOrThrow(id);

		validatePostOwner(post);
		
		for (String tag : request.getTags()) {

			if (!post.getTags().contains(tag)) {

				post.getTags().add(tag);
			}
		}

		Post updatedPost = postRepository.save(post);
		return postMapper.toResponseDto(updatedPost);
	}

	@Override
	@Transactional
	public PostResponseDto removeTag(String id, RemoveTagsRequestDto request) {

		Post post = findPostbyIdOrThrow(id);

		validatePostOwner(post);
		
		post.getTags().removeAll(request.getTags());

		Post updatedPost = postRepository.save(post);
		return postMapper.toResponseDto(updatedPost);
	}

	@Override
	@Transactional
	@CacheEvict(value = "post", key = "#id")
	public void deletePost(String id) {

		Post post = findPostbyIdOrThrow(id);
		validatePostOwner(post);
		
		postRepository.delete(post);
	}

	@Override
	@Transactional
	public PostResponseDto publishPost(String id) {

		Post post = findPostbyIdOrThrow(id);

		validatePostOwner(post);
		
		post.setStatus(PostStatus.PUBLISHED);

		Post publishedPost = postRepository.save(post);
		return postMapper.toResponseDto(publishedPost);
	}

	@Override
	@Transactional
	public PostResponseDto draftPost(String id) {

		Post post = findPostbyIdOrThrow(id);

		validatePostOwner(post);
		
		post.setStatus(PostStatus.DRAFT);

		Post draftPost = postRepository.save(post);
		return postMapper.toResponseDto(draftPost);
	}

	@Override
	@Transactional
	public Post findPostbyIdOrThrow(String id) {

		return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post does not exist."));
	}

	@Override
	@Transactional
	public PageResponse<PostResponseDto> getPosts(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Post::getCreatedAt).descending());

		Page<Post> postsPage = postRepository.findAll(pageable);

		return new PageResponse<>(
				postsPage.getContent().stream().map(postMapper::toResponseDto).toList(),
				   postsPage.getNumber(),
			        postsPage.getSize(),
			        postsPage.getTotalElements(),
			        postsPage.getTotalPages(),
			        postsPage.isFirst(),
			        postsPage.isLast(),
			        postsPage.getNumberOfElements());

	}

	@Override
	@Transactional
	public List<PostResponseDto> searchPosts(SearchPostRequest request) {

		List<Post> posts = postRepository.findAll(PostSpecification.search(request));

		return postMapper.toResponseDtoList(posts);

	}

	@Override
	@Transactional
	public PostResponseDto addComment(String id, CreateCommentRequestDto request) {

		Post post = findPostbyIdOrThrow(id);

		Comment comment = new Comment();
		comment.setId(UUID.randomUUID().toString());
		comment.setCommenterName(request.getCommenterName());
		comment.setContent(request.getContent());
		comment.setCreatedAt(LocalDateTime.now());

		post.getComments().add(comment);
		Post savedPost = postRepository.save(post);
		return postMapper.toResponseDto(savedPost);
	}

	@Override
	@Transactional
	public List<StatusStatisticsResponseDto> getStatusStatistics() {

		return postRepository.getStatusStatistics();
	}

	@Override
	@Transactional
	public List<AuthorStatisticsResponseDto> getAuthorStatistics() {

		return postRepository.getAuthorStatistics();
	}

	@Override
	@Transactional
	public List<CategoryStatisticsResponseDto> getCategoryStatistics() {

		return postRepository.getCategoryStatistics();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PostResponseDto> getLatestPosts(int limit) {

		Pageable pageable = PageRequest.of(0, limit, Sort.by(Post::getCreatedAt).descending());

		return postRepository.findAll(pageable).getContent().stream().map(postMapper::toResponseDto).toList();
	}

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}
	
	private void validatePostOwner(Post post) {
		User currentUser = getCurrentUser();
			
			if (!post.getUser().getId().equals(currentUser.getId())
			        && !currentUser.getRole().equals(Role.ADMIN)) {
			    throw new ForbiddenException("You are not allowed to modify this post.");
			}
		}
		
}
