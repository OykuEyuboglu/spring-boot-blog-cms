package com.oyku.blog.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.oyku.blog.dto.request.CreatePostRequestDto;
import com.oyku.blog.dto.request.UpdatePostRequestDto;
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

    @Mapping(target = "tags", expression = "java(copyTags(post.getTags()))")
    PostResponseDto toResponseDto(Post post);

    List<PostResponseDto> toResponseDtoList(List<Post> posts);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdatePostRequestDto updatePostRequestDto, @MappingTarget Post post);

    default List<String> copyTags(List<String> tags) {
        return tags != null ? new ArrayList<>(tags) : new ArrayList<>();
    }
}