package com.oyku.blog.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.oyku.blog.dto.request.comment.CreateCommentRequestDto;
import com.oyku.blog.dto.request.post.CreatePostRequestDto;
import com.oyku.blog.dto.request.post.RemoveTagsRequestDto;
import com.oyku.blog.dto.request.post.SearchPostRequest;
import com.oyku.blog.dto.request.post.UpdatePostRequestDto;
import com.oyku.blog.dto.request.post.UpdateTagsRequestDto;
import com.oyku.blog.dto.response.comment.CommentResponseDto;
import com.oyku.blog.dto.response.post.PostResponseDto;
import com.oyku.blog.entity.Category;
import com.oyku.blog.entity.Post;
import com.oyku.blog.enums.PostStatus;
import com.oyku.blog.exception.ResourceNotFoundException;
import com.oyku.blog.mapper.CommentMapperImpl;
import com.oyku.blog.mapper.PostMapper;
import com.oyku.blog.model.Comment;
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
	void shouldReturnAllPostsSuccessfully() {
		Post post = createPost();
		PostResponseDto response = createResponse();

		when(postRepository.findAll()).thenReturn(List.of(post));
		when(postMapper.toResponseDtoList(List.of(post))).thenReturn(List.of(response));

		List<PostResponseDto> result = postService.getAllPosts();

		assertEquals(1, result.size());
		assertEquals(response.getTitle(), result.get(0).getTitle());

		verify(postRepository).findAll();
		verify(postMapper).toResponseDtoList(List.of(post));
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
	void shouldNotGenerateSlugWhenTitleIsBlank() {
		String postId = "1";
		UpdatePostRequestDto request = new UpdatePostRequestDto();

		request.setTitle("");

		Post post = createPost();
		PostResponseDto response = createResponse();

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.updatePost(postId, request);

		assertNotNull(result);

		verify(slugService, never()).generateSlug(anyString());
	}

	@Test
	void shouldNotGenerateSlugWhenTitleIsNull() {
		String postId = "1";
		UpdatePostRequestDto request = new UpdatePostRequestDto();

		request.setTitle(null);

		Post post = createPost();
		PostResponseDto response = createResponse();

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.updatePost(postId, request);

		assertNotNull(result);

		verify(slugService, never()).generateSlug(anyString());
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
	void shouldDraftPostSuccessfully() {

		String postId = "1";
		Post post = createPost();
		post.setStatus(PostStatus.PUBLISHED);
		PostResponseDto response = createResponse();

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.draftPost(postId);

		verify(postRepository).findById(postId);
		verify(postMapper).toResponseDto(post);
		verify(postRepository).save(post);

		assertEquals(PostStatus.DRAFT, post.getStatus());
		assertEquals(response, result);
	}

	@Test
	void shouldThrowExceptionWhenDraftingNonExistingPost() {

		String postId = "1";

		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> postService.draftPost(postId));

		assertEquals("Post does not exist.", exception.getMessage());

		verify(postMapper, never()).toResponseDto(any());
		verify(postRepository).findById(postId);
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

	@Test
	void shouldReturnCommentsByPostIdSuccessfully() {

		String postId = "1";
		Post post = createPost();

		Comment comment = new Comment();
		post.setComments(List.of(comment));

		CommentResponseDto response = new CommentResponseDto();

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		when(commentMapperImpl.toResponseDtos(post.getComments())).thenReturn(List.of(response));

		List<CommentResponseDto> result = postService.getCommentsByPostId(postId);
		assertEquals(1, result.size());

		verify(commentMapperImpl).toResponseDtos(post.getComments());
	}

	@Test
	void shouldThrowExceptionWhenGettingCommentsOfNonExistingPost() {
		String postId = "1";

		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> postService.getCommentsByPostId(postId));

		verify(commentMapperImpl, never()).toResponseDto(any());
	}

	@Test
	void shouldAddCommentSuccessfully() {

		String postId = "1";

		CreateCommentRequestDto request = new CreateCommentRequestDto();

		request.setCommenterName("Commenter Name");
		request.setContent("Test Comment");

		Post post = createPost();
		PostResponseDto response = createResponse();

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.addComment(postId, request);

		assertNotNull(result);
		assertEquals(1, post.getComments().size());
		assertEquals(request.getCommenterName(), post.getComments().get(0).getCommenterName());
		assertEquals(request.getContent(), post.getComments().get(0).getContent());

		verify(postRepository).save(post);
		verify(postMapper).toResponseDto(post);
	}

	@Test
	void shouldAddTagsSuccessfully() {

		String postId = "1";

		UpdateTagsRequestDto request = new UpdateTagsRequestDto();

		request.setTags(List.of("tag1"));

		Post post = createPost();
		PostResponseDto response = createResponse();

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.addTags(postId, request);

		assertNotNull(result);
		assertEquals(1, post.getTags().size());
		assertEquals(request.getTags().get(0), post.getTags().get(0));
		assertTrue(post.getTags().contains("tag1"));

		verify(postRepository).save(post);
		verify(postRepository).findById(postId);
		verify(postMapper).toResponseDto(post);
	}

	@Test
	void shouldThrowExceptionWhenAddingTagToNonExistingPost() {

		String postId = "1";

		UpdateTagsRequestDto request = new UpdateTagsRequestDto();
		request.setTags(List.of("tag1"));

		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> postService.addTags(postId, request));

	}

	@Test
	void shouldNotAddDuplicateTag() {

		String postId = "1";

		Post post = createPost();
		post.setTags(new ArrayList<>(List.of("tag1")));

		PostResponseDto response = createResponse();

		UpdateTagsRequestDto request = new UpdateTagsRequestDto();
		request.setTags(List.of("tag1"));

		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.addTags(postId, request);

		assertNotNull(result);
		assertEquals(1, post.getTags().size());
		assertTrue(post.getTags().contains("tag1"));

		verify(postRepository).findById(postId);
		verify(postRepository).save(post);
		verify(postMapper).toResponseDto(post);
	}

	@Test
	void shouldRemoveTagsSuccessfully() {

		String postId = "1";
		Post post = createPost();

		RemoveTagsRequestDto request = new RemoveTagsRequestDto();

		post.setTags(new ArrayList<>(List.of("tag1", "tag2")));

		request.setTags(List.of("tag1"));

		PostResponseDto response = createResponse();

		when(postMapper.toResponseDto(post)).thenReturn(response);
		when(postRepository.findById(postId)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);

		PostResponseDto result = postService.removeTag(postId, request);

		assertNotNull(result);
		assertFalse(post.getTags().contains("tag1"));
		assertEquals(1, post.getTags().size());

		verify(postRepository).save(post);
		verify(postRepository).findById(postId);
		verify(postMapper).toResponseDto(post);

	}

	@Test
	void shouldSearchPostsSuccessfully() {

		SearchPostRequest request = new SearchPostRequest();

		List<Post> posts = List.of(createPost());
		List<PostResponseDto> response = List.of(createResponse());

		when(postRepository.findAll(ArgumentMatchers.<Specification<Post>>any())).thenReturn(posts);
		when(postMapper.toResponseDtoList(posts)).thenReturn(response);

		List<PostResponseDto> result = postService.searchPosts(request);

		assertNotNull(result);
		assertEquals(1, result.size());

		verify(postRepository).findAll(ArgumentMatchers.<Specification<Post>>any());
		verify(postMapper).toResponseDtoList(posts);
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
		post.setTags(new ArrayList<>());
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