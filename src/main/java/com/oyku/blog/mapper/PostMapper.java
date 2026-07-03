package com.oyku.blog.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.oyku.blog.dto.request.CreatePostRequestDto;
import com.oyku.blog.dto.response.PostResponseDto;
import com.oyku.blog.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Post toEntity(CreatePostRequestDto createPostRequestDto);

    @Mapping(
        target = "tags",
        expression = "java(post.getTags() != null ? new ArrayList<>(post.getTags()) : new ArrayList<>())"
    )
    PostResponseDto toResponseDto(Post post);

    List<PostResponseDto> toResponseDtoList(List<Post> posts);
}