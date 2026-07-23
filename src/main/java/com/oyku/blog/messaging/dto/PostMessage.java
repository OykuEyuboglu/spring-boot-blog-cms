package com.oyku.blog.messaging.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostMessage {

	private String postId;
	private String title;
	private String authorName;
	private String status;
	private LocalDateTime publishedAt;
}
