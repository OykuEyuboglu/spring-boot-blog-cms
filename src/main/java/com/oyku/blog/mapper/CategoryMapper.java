package com.oyku.blog.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.oyku.blog.dto.request.category.CreateCategoryRequestDto;
import com.oyku.blog.dto.request.category.UpdateCategoryRequestDto;
import com.oyku.blog.dto.response.category.CategoryResponseDto;
import com.oyku.blog.entity.Category;

@Mapper(componentModel = "spring")

public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
	Category toEntity(CreateCategoryRequestDto request);

    CategoryResponseDto toResponseDto(Category category);

    List<CategoryResponseDto> toResponseDtoList(List<Category> categories);
    
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateCategoryFromDto(UpdateCategoryRequestDto request,
                               @MappingTarget Category category);
}
