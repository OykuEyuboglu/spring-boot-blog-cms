package com.oyku.blog.dto.response.statistics;

import com.oyku.blog.enums.PostStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusStatisticsResponseDto {
	
	private PostStatus status;
	private Long postCount;

}
