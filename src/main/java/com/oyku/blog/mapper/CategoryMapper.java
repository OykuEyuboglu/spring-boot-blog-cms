package com.oyku.blog.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.oyku.blog.dto.request.category.CreateCategoryRequestDto;
import com.oyku.blog.dto.response.category.CategoryResponseDto;
import com.oyku.blog.entity.Category;

@Mapper(componentModel = "spring")

public interface CategoryMapper {

	Category toEntity(CreateCategoryRequestDto request);

    CategoryResponseDto toResponseDto(Category category);

    List<CategoryResponseDto> toResponseDtoList(List<Category> categories);
}
