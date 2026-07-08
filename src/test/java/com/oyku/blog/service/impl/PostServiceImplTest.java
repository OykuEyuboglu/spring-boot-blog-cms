package com.oyku.blog.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oyku.blog.dto.request.post.CreatePostRequestDto;
import com.oyku.blog.dto.request.post.UpdatePostRequestDto;
import com.oyku.blog.dto.response.post.PostResponseDto;
import com.oyku.blog.entity.Category;
import com.oyku.blog.entity.Post;
import com.oyku.blog.enums.PostStatus;
import com.oyku.blog.exception.ResourceNotFoundException;
import com.oyku.blog.mapper.CommentMapperImpl;
import com.oyku.blog.mapper.PostMapper;
import com.oyku.blog.repository.CategoryRepository;
import com.oyku.blog.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

	@Mock
	private PostRepository postRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private PostMapper postMapper;

	@Mock
	private SlugService slugService;

	@Mock
	private CommentMapperImpl commentMapperImpl;

	@InjectMocks
	private PostServiceImpl postService;

	@Test
	void shouldCreatePostSuccessfully() {

		CreatePostRequestDto request = createRequest();
		Post post = createPost();
		Category category = createCategory();
		PostResponseDto response = createResponse();

		when(postMapper.toEntity(request)).thenReturn(post);

		when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));

		when(slugService.generateSlug(request.getTitle())).thenReturn(response.getSlug());

		when(postRepository.save(post)).thenReturn(post);

		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.createPost(request);

		assertNotNull(result);
		assertEquals("Test Title", result.getTitle());
		assertEquals(PostStatus.DRAFT, post.getStatus());
		assertEquals("test-title", result.getSlug());

		verify(slugService).generateSlug(request.getTitle());
		verify(postRepository).save(post);
		verify(slugService).generateSlug(response.getTitle());

		verify(postMapper).toEntity(request);
		verify(postMapper).toResponseDto(post);
		verify(categoryRepository).findById(request.getCategoryId());

	}

	@Test
	void shouldThrowExceptionWhenCategoryNotFound() {
		CreatePostRequestDto request = createRequest();
		Post post = createPost();

		when(postMapper.toEntity(request)).thenReturn(post);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> postService.createPost(request));

		assertEquals("Category does not exist.", exception.getMessage());

		verify(categoryRepository).findById(request.getCategoryId());
		verify(postRepository, never()).save(any(Post.class));

	}

	@Test
	void shouldReturnPostByIdSuccessfully() {

		Post post = createPost();
		PostResponseDto response = createResponse();

		String postId = "1";

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.getPostById(postId);

		assertNotNull(result);
		assertEquals(response.getTitle(), result.getTitle());

		verify(postRepository).findById(postId);
		verify(postMapper).toResponseDto(post);
	}

	@Test
	void shouldThrowExceptionWhenPostNotFound() {
		String postId = "1";

		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> postService.getPostById(postId));

		assertEquals("Post does not exist.", exception.getMessage());

		verify(postRepository).findById(postId);
		verify(postMapper, never()).toResponseDto(any());
	}

	@Test
	void shouldUpdatePostSuccessfully() {

		String postId = "1";

		UpdatePostRequestDto request = new UpdatePostRequestDto();

		request.setTitle("New Title");

		Post post = createPost();
		PostResponseDto response = createResponse();
		response.setTitle("New Title");
		response.setSlug("new-title");

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		when(slugService.generateSlug(request.getTitle())).thenReturn(response.getSlug());

		when(postRepository.save(post)).thenReturn(post);

		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.updatePost(postId, request);

		assertNotNull(result);
		assertEquals(response.getTitle(), result.getTitle());
		assertEquals(response.getSlug(), post.getSlug());

		verify(postRepository).findById(postId);

		verify(postMapper).updateEntityFromDto(request, post);

		verify(slugService).generateSlug(response.getTitle());

		verify(postRepository).save(post);
		verify(postMapper).toResponseDto(post);
	}

	@Test
	void shouldThrowExceptionWhenUpdatingNonExistingPost() {

		String postId = "1";
		UpdatePostRequestDto request = new UpdatePostRequestDto();

		request.setTitle("New Title");

		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> postService.updatePost(postId, request));

		assertEquals("Post does not exist.", exception.getMessage());

		verify(postRepository).findById(postId);
		verify(postMapper, never()).updateEntityFromDto(any(), any());
		verify(postRepository, never()).save(any(Post.class));
	}

	@Test
	void shouldDeletePostSuccessfully() {

		String postId = "1";
		Post post = createPost();

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		postService.deletePost(postId);

		verify(postRepository).findById(postId);
		verify(postRepository).delete(post);
	}

	@Test
	void shouldThrowExceptionWhenDeletingNonExistingPost() {

		String postId = "1";

		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> postService.deletePost(postId));

		assertEquals("Post does not exist.", exception.getMessage());

		verify(postRepository).findById(postId);
		verify(postMapper, never()).updateEntityFromDto(any(), any());
		verify(postRepository, never()).save(any(Post.class));
	}

	@Test
	void shouldPublishPostSuccessfully() {

		String postId = "1";
		Post post = createPost();
		PostResponseDto response = createResponse();

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.publishPost(postId);

		verify(postRepository).findById(postId);
		verify(postMapper).toResponseDto(post);
		verify(postRepository).save(post);

		assertEquals(PostStatus.PUBLISHED, post.getStatus());
		assertEquals(response, result);
	}

	@Test
	void shouldThrowExceptionWhenPublishingNonExistingPost() {

		String postId = "1";

		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> postService.publishPost(postId));

		assertEquals("Post does not exist.", exception.getMessage());

		verify(postMapper, never()).toResponseDto(any());
		verify(postRepository).findById(postId);
		verify(postRepository, never()).save(any(Post.class));
	}

	private CreatePostRequestDto createRequest() {
		CreatePostRequestDto request = new CreatePostRequestDto();
		request.setTitle("Test Title");
		request.setCategoryId(1L);
		return request;
	}

	private Post createPost() {
		Post post = new Post();
		post.setTitle("Test Title");
		return post;
	}

	private Category createCategory() {
		Category category = new Category();
		category.setId(1L);
		return category;
	}

	private PostResponseDto createResponse() {
		PostResponseDto response = new PostResponseDto();
		response.setTitle("Test Title");
		response.setSlug("test-title");
		return response;
	}

}