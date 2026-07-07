package com.oyku.blog.service;

import java.util.List;

import com.oyku.blog.dto.request.category.CreateCategoryRequestDto;
import com.oyku.blog.dto.response.category.CategoryResponseDto;

public interface CategoryService {

	List<CategoryResponseDto> getAllCategories();

	CategoryResponseDto createCategory(CreateCategoryRequestDto request);

}
