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

import com.oyku.blog.dto.request.category.CreateCategoryRequestDto;
import com.oyku.blog.dto.request.category.UpdateCategoryRequestDto;
import com.oyku.blog.dto.response.category.CategoryResponseDto;
import com.oyku.blog.entity.Category;
import com.oyku.blog.exception.ResourceNotFoundException;
import com.oyku.blog.mapper.CategoryMapper;
import com.oyku.blog.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private CategoryMapper categoryMapper;

	@InjectMocks
	private CategoryServiceImpl categoryService;

	@Test
	void shouldCreateCategorySuccesfully() {
		CreateCategoryRequestDto request = createRequest();
		Category category = createCategory();

		CategoryResponseDto response = createResponse();

		when(categoryMapper.toEntity(request)).thenReturn(category);

		when(categoryRepository.save(category)).thenReturn(category);
		when(categoryMapper.toResponseDto(category)).thenReturn(response);

		CategoryResponseDto result = categoryService.createCategory(request);

		assertNotNull(result);
		assertEquals("Test Name", result.getName());

		verify(categoryRepository).save(category);

		verify(categoryMapper).toEntity(request);
		verify(categoryMapper).toResponseDto(category);
		
		assertEquals(response.getName(), result.getName());
		}

	@Test
	void shouldReturnCategoryByIdSuccessfully() {

		Category category = createCategory();
		CategoryResponseDto response = createResponse();

		Long categoryId = 1L;

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		when(categoryMapper.toResponseDto(category)).thenReturn(response);

		CategoryResponseDto result = categoryService.getCategoryById(categoryId);

		assertNotNull(result);
		assertEquals(response.getName(), result.getName());

		verify(categoryRepository).findById(categoryId);
		verify(categoryMapper).toResponseDto(category);
	}

	@Test
	void shouldThrowExceptionWhenCategoryNotFound() {
		Long categoryId = 1L;

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> categoryService.getCategoryById(categoryId));

		assertEquals("Category does not exist.", exception.getMessage());

		verify(categoryRepository).findById(categoryId);
		verify(categoryMapper, never()).toResponseDto(any());
	}

	@Test
	void shouldUpdateCategorySuccessfully() {

		Long categoryId = 1L;

		UpdateCategoryRequestDto request = new UpdateCategoryRequestDto();

		request.setName("New Name");

		Category category = createCategory();
		CategoryResponseDto response = createResponse();
		response.setName("New Name");

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

		when(categoryRepository.save(category)).thenReturn(category);

		when(categoryMapper.toResponseDto(category)).thenReturn(response);

		CategoryResponseDto result = categoryService.updateCategory(categoryId, request);

		assertNotNull(result);
		assertEquals(response.getName(), result.getName());

		verify(categoryRepository).findById(categoryId);

		verify(categoryMapper).updateCategoryFromDto(request, category);

		verify(categoryRepository).save(category);
		verify(categoryMapper).toResponseDto(category);
	}

	@Test
	void shouldThrowExceptionWhenUpdatingNonExistingCategory() {

		Long categoryId = 1L;
		UpdateCategoryRequestDto request = new UpdateCategoryRequestDto();

		request.setName("New Name");

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> categoryService.updateCategory(categoryId, request));

		assertEquals("Category does not exist.", exception.getMessage());

		verify(categoryRepository).findById(categoryId);
		verify(categoryRepository, never()).delete(any(Category.class));
		verify(categoryRepository, never()).save(any(Category.class));
	}

	@Test
	void shouldDeleteCategorySuccessfully() {

		Long categoryId = 1L;
		Category category = createCategory();

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

		categoryService.deleteCategory(categoryId);

		verify(categoryRepository).findById(categoryId);
		verify(categoryRepository).delete(category);
	}

	@Test
	void shouldThrowExceptionWhenDeletingNonExistingCategory() {

		Long categoryId = 1L;

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> categoryService.deleteCategory(categoryId));

		assertEquals("Category does not exist.", exception.getMessage());

		verify(categoryRepository).findById(categoryId);
		verify(categoryMapper, never()).updateCategoryFromDto(any(), any());
		verify(categoryRepository, never()).save(any(Category.class));
	}
	
	@Test
	void shouldReturnAllCategorySuccessfully(){
		
	}

	private CreateCategoryRequestDto createRequest() {
		CreateCategoryRequestDto request = new CreateCategoryRequestDto();
		request.setName("Test Name");
		return request;
	}

	private Category createCategory() {
		Category category = new Category();
		category.setId(1L);
		category.setName("Test Name");
		return category;
	}

	private CategoryResponseDto createResponse() {
		CategoryResponseDto response = new CategoryResponseDto();
		response.setName("Test Name");
		return response;
	}
}
