package com.oyku.blog.service;

import java.util.List;

import com.oyku.blog.dto.request.category.CreateCategoryRequestDto;
import com.oyku.blog.dto.request.category.UpdateCategoryRequestDto;
import com.oyku.blog.dto.response.category.CategoryResponseDto;
import com.oyku.blog.entity.Category;

public interface CategoryService {

	List<CategoryResponseDto> getAllCategories();

	CategoryResponseDto createCategory(CreateCategoryRequestDto request);

	CategoryResponseDto getCategoryById(Long id);

	CategoryResponseDto updateCategory(Long id, UpdateCategoryRequestDto request);

	Category findCategorybyIdOrThrow(Long id);

	void deleteCategory(Long id);

}
