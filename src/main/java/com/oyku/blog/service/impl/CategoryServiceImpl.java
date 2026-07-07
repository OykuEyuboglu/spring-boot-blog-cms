package com.oyku.blog.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oyku.blog.dto.request.category.CreateCategoryRequestDto;
import com.oyku.blog.dto.response.category.CategoryResponseDto;
import com.oyku.blog.entity.Category;
import com.oyku.blog.mapper.CategoryMapper;
import com.oyku.blog.repository.CategoryRepository;
import com.oyku.blog.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
	@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	

	    private final CategoryRepository categoryRepository;
	    private final CategoryMapper categoryMapper;

	    @Override
	    public CategoryResponseDto createCategory(CreateCategoryRequestDto request) {

	        Category category = categoryMapper.toEntity(request);

	        category = categoryRepository.save(category);

	        return categoryMapper.toResponseDto(category);
	    }

	    @Override
	    public List<CategoryResponseDto> getAllCategories() {

	        List<Category> categories = categoryRepository.findAll();

	        return categoryMapper.toResponseDtoList(categories);
	    }
	
}
