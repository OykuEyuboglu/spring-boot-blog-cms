package com.oyku.blog.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oyku.blog.dto.request.category.CreateCategoryRequestDto;
import com.oyku.blog.dto.request.category.UpdateCategoryRequestDto;
import com.oyku.blog.dto.response.category.CategoryResponseDto;
import com.oyku.blog.entity.Category;
import com.oyku.blog.exception.ResourceNotFoundException;
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
	    
	    
	    @Override
		@Transactional(readOnly = true)
		public CategoryResponseDto getCategoryById(Long id) {

			Category category = findCategorybyIdOrThrow(id);

			return categoryMapper.toResponseDto(category);
		}
	    
	    
	    
	    @Override
		@Transactional
		public CategoryResponseDto updateCategory(Long id, UpdateCategoryRequestDto request) {

			Category category = findCategorybyIdOrThrow(id);
			
			categoryMapper.updateCategoryFromDto(request, category);
				
			Category updatedCategory = categoryRepository.save(category);

			return categoryMapper.toResponseDto(updatedCategory);
		}

	    
	    
		@Override
		@Transactional
		public void deleteCategory(Long id) {

			Category category = findCategorybyIdOrThrow(id);

			categoryRepository.delete(category);
		}

	    
		@Override
		@Transactional
		public Category findCategorybyIdOrThrow(Long id) {

			return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category does not exist."));
		}
	    
	
}
