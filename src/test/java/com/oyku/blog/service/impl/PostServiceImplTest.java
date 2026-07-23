package com.oyku.blog.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
import com.oyku.blog.entity.User;
import com.oyku.blog.enums.PostStatus;
import com.oyku.blog.enums.Role;
import com.oyku.blog.exception.ResourceNotFoundException;
import com.oyku.blog.mapper.CommentMapperImpl;
import com.oyku.blog.mapper.PostMapper;
import com.oyku.blog.messaging.dto.PostMessage;
import com.oyku.blog.messaging.producer.PostProducer;
import com.oyku.blog.model.Comment;
import com.oyku.blog.repository.CategoryRepository;
import com.oyku.blog.repository.PostRepository;
import com.oyku.blog.repository.UserRepository;

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
	
	@Mock
	private PostProducer postProducer;
	
	@InjectMocks
	private PostServiceImpl postService;

	@Mock
	private UserRepository userRepository;
	
	private static final String POST_ID = "1";
	private User testUser;

	@BeforeEach
	void setUp() {

	    testUser = new User();
	    testUser.setId(1L);
	    testUser.setEmail("test@example.com");
	    testUser.setRole(Set.of(Role.ADMIN));  
	}
	
	@AfterEach
	void tearDown() {
	    SecurityContextHolder.clearContext();
	}
	
	private void mockAuthentication() {
		  Authentication authentication = mock(Authentication.class);
		    when(authentication.getName()).thenReturn(testUser.getEmail());

		    SecurityContext securityContext = mock(SecurityContext.class);
		    when(securityContext.getAuthentication()).thenReturn(authentication);

		    SecurityContextHolder.setContext(securityContext);

		    when(userRepository.findByEmail(testUser.getEmail()))
		            .thenReturn(Optional.of(testUser));
	}
	
	@Test
	void shouldCreatePostSuccessfully() {

		mockAuthentication();

		CreatePostRequestDto request = createRequest();
		Post post = createPost();
		Category category = createCategory();
		PostResponseDto response = createResponse();

		doNothing().when(postProducer).sendPostCreated(any(PostMessage.class));
		
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
		verify(postProducer).sendPostCreated(any(PostMessage.class));
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

		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.getPostById(POST_ID);

		assertNotNull(result);
		assertEquals(response.getTitle(), result.getTitle());

		verify(postRepository).findById(POST_ID);
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

		when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> postService.getPostById(POST_ID));

		assertEquals("Post does not exist.", exception.getMessage());

		verify(postRepository).findById(POST_ID);
		verify(postMapper, never()).toResponseDto(any());
	}

	@Test
	void shouldUpdatePostSuccessfully() {

		mockAuthentication();

		UpdatePostRequestDto request = new UpdatePostRequestDto();

		request.setTitle("New Title");

		Post post = createPost();
		PostResponseDto response = createResponse();
		response.setTitle("New Title");
		response.setSlug("new-title");

		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));

		when(slugService.generateSlug(request.getTitle())).thenReturn(response.getSlug());

		when(postRepository.save(post)).thenReturn(post);

		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.updatePost(POST_ID, request);

		assertNotNull(result);
		assertEquals(response.getTitle(), result.getTitle());
		assertEquals(response.getSlug(), post.getSlug());

		verify(postRepository).findById(POST_ID);

		verify(postMapper).updateEntityFromDto(request, post);

		verify(slugService).generateSlug(response.getTitle());

		verify(postRepository).save(post);
		verify(postMapper).toResponseDto(post);
	}

	@Test
	void shouldThrowExceptionWhenUpdatingNonExistingPost() {

		UpdatePostRequestDto request = new UpdatePostRequestDto();

		request.setTitle("New Title");

		when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> postService.updatePost(POST_ID, request));

		assertEquals("Post does not exist.", exception.getMessage());

		verify(postRepository).findById(POST_ID);
		verify(postMapper, never()).updateEntityFromDto(any(), any());
		verify(postRepository, never()).save(any(Post.class));
	}

	@Test
	void shouldNotGenerateSlugWhenTitleIsBlank() {
		
		mockAuthentication();

		UpdatePostRequestDto request = new UpdatePostRequestDto();

		request.setTitle("");

		Post post = createPost();
		PostResponseDto response = createResponse();

		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.updatePost(POST_ID, request);

		assertNotNull(result);

		verify(slugService, never()).generateSlug(anyString());
	}

	@Test
	void shouldNotGenerateSlugWhenTitleIsNull() {
		
		mockAuthentication();

		UpdatePostRequestDto request = new UpdatePostRequestDto();

		request.setTitle(null);

		Post post = createPost();
		PostResponseDto response = createResponse();

		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.updatePost(POST_ID, request);

		assertNotNull(result);

		verify(slugService, never()).generateSlug(anyString());
	}

	@Test
	void shouldDeletePostSuccessfully() {

		mockAuthentication();

		Post post = createPost();

		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));

		postService.deletePost(POST_ID);

		verify(postRepository).findById(POST_ID);
		verify(postRepository).delete(post);
	}

	@Test
	void shouldThrowExceptionWhenDeletingNonExistingPost() {

		when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> postService.deletePost(POST_ID));

		assertEquals("Post does not exist.", exception.getMessage());

		verify(postRepository).findById(POST_ID);
		verify(postMapper, never()).updateEntityFromDto(any(), any());
		verify(postRepository, never()).save(any(Post.class));
	}

	@Test
	void shouldDraftPostSuccessfully() {

		mockAuthentication();

		Post post = createPost();
		post.setStatus(PostStatus.PUBLISHED);
		PostResponseDto response = createResponse();

		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.draftPost(POST_ID);

		verify(postRepository).findById(POST_ID);
		verify(postMapper).toResponseDto(post);
		verify(postRepository).save(post);

		assertEquals(PostStatus.DRAFT, post.getStatus());
		assertEquals(response, result);
	}

	@Test
	void shouldThrowExceptionWhenDraftingNonExistingPost() {

		when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> postService.draftPost(POST_ID));

		assertEquals("Post does not exist.", exception.getMessage());

		verify(postMapper, never()).toResponseDto(any());
		verify(postRepository).findById(POST_ID);
		verify(postRepository, never()).save(any(Post.class));
	}

	@Test
	void shouldPublishPostSuccessfully() {
		
		mockAuthentication();

		Post post = createPost();
		PostResponseDto response = createResponse();

		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.publishPost(POST_ID);

		verify(postRepository).findById(POST_ID);
		verify(postMapper).toResponseDto(post);
		verify(postRepository).save(post);

		assertEquals(PostStatus.PUBLISHED, post.getStatus());
		assertEquals(response, result);
	}

	@Test
	void shouldThrowExceptionWhenPublishingNonExistingPost() {

		when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> postService.publishPost(POST_ID));

		assertEquals("Post does not exist.", exception.getMessage());

		verify(postMapper, never()).toResponseDto(any());
		verify(postRepository).findById(POST_ID);
		verify(postRepository, never()).save(any(Post.class));
	}

	@Test
	void shouldReturnCommentsByPostIdSuccessfully() {

		Post post = createPost();

		Comment comment = new Comment();
		post.setComments(List.of(comment));

		CommentResponseDto response = new CommentResponseDto();

		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));

		when(commentMapperImpl.toResponseDtos(post.getComments())).thenReturn(List.of(response));

		List<CommentResponseDto> result = postService.getCommentsByPostId(POST_ID);
		assertEquals(1, result.size());

		verify(commentMapperImpl).toResponseDtos(post.getComments());
	}

	@Test
	void shouldThrowExceptionWhenGettingCommentsOfNonExistingPost() {

		when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> postService.getCommentsByPostId(POST_ID));

		verify(commentMapperImpl, never()).toResponseDto(any());
	}

	@Test
	void shouldAddCommentSuccessfully() {

		CreateCommentRequestDto request = new CreateCommentRequestDto();

		request.setCommenterName("Commenter Name");
		request.setContent("Test Comment");

		Post post = createPost();
		PostResponseDto response = createResponse();

		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.addComment(POST_ID, request);

		assertNotNull(result);
		assertEquals(1, post.getComments().size());
		assertEquals(request.getCommenterName(), post.getComments().get(0).getCommenterName());
		assertEquals(request.getContent(), post.getComments().get(0).getContent());

		verify(postRepository).save(post);
		verify(postMapper).toResponseDto(post);
	}

	@Test
	void shouldAddTagsSuccessfully() {
		
		mockAuthentication();

		UpdateTagsRequestDto request = new UpdateTagsRequestDto();

		request.setTags(List.of("tag1"));

		Post post = createPost();
		PostResponseDto response = createResponse();

		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.addTags(POST_ID, request);

		assertNotNull(result);
		assertEquals(1, post.getTags().size());
		assertEquals(request.getTags().get(0), post.getTags().get(0));
		assertTrue(post.getTags().contains("tag1"));

		verify(postRepository).save(post);
		verify(postRepository).findById(POST_ID);
		verify(postMapper).toResponseDto(post);
	}

	@Test
	void shouldThrowExceptionWhenAddingTagToNonExistingPost() {

		UpdateTagsRequestDto request = new UpdateTagsRequestDto();
		request.setTags(List.of("tag1"));

		when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> postService.addTags(POST_ID, request));

	}

	@Test
	void shouldNotAddDuplicateTag() {

		mockAuthentication();

		Post post = createPost();
		post.setTags(new ArrayList<>(List.of("tag1")));

		PostResponseDto response = createResponse();

		UpdateTagsRequestDto request = new UpdateTagsRequestDto();
		request.setTags(List.of("tag1"));

		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.toResponseDto(post)).thenReturn(response);

		PostResponseDto result = postService.addTags(POST_ID, request);

		assertNotNull(result);
		assertEquals(1, post.getTags().size());
		assertTrue(post.getTags().contains("tag1"));

		verify(postRepository).findById(POST_ID);
		verify(postRepository).save(post);
		verify(postMapper).toResponseDto(post);
	}

	@Test
	void shouldRemoveTagsSuccessfully() {

		mockAuthentication();

		Post post = createPost();

		RemoveTagsRequestDto request = new RemoveTagsRequestDto();

		post.setTags(new ArrayList<>(List.of("tag1", "tag2")));

		request.setTags(List.of("tag1"));

		PostResponseDto response = createResponse();

		when(postMapper.toResponseDto(post)).thenReturn(response);
		when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
		when(postRepository.save(post)).thenReturn(post);

		PostResponseDto result = postService.removeTag(POST_ID, request);

		assertNotNull(result);
		assertFalse(post.getTags().contains("tag1"));
		assertEquals(1, post.getTags().size());

		verify(postRepository).save(post);
		verify(postRepository).findById(POST_ID);
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
		post.setUser(testUser);
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