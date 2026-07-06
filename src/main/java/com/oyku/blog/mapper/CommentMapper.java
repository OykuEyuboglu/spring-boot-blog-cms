package com.oyku.blog.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.oyku.blog.dto.response.comment.CommentResponseDto;
import com.oyku.blog.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

	CommentResponseDto toResponseDto(Comment comment);
	List<CommentResponseDto> toResponseDtos(List<Comment> comments);
}
