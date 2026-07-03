package com.oyku.blog.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.oyku.blog.dto.request.post.CreatePostRequestDto;
import com.oyku.blog.dto.request.post.UpdatePostRequestDto;
import com.oyku.blog.dto.response.post.PostResponseDto;
import com.oyku.blog.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    Post toEntity(CreatePostRequestDto createPostRequestDto);

    @Mapping(target = "tags", expression = "java(copyTags(post.getTags()))")
    @Mapping(source = "category.id", target="categoryId" )
    PostResponseDto toResponseDto(Post post);

    
    
    List<PostResponseDto> toResponseDtoList(List<Post> posts);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromDto(UpdatePostRequestDto updatePostRequestDto, @MappingTarget Post post);

    @Condition
    default boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    @Condition
    default boolean isNotEmpty(List<?> value) {
        return value != null && !value.isEmpty();
    }

    default List<String> copyTags(List<String> tags) {
        return tags != null ? new ArrayList<>(tags) : new ArrayList<>();
    }
}