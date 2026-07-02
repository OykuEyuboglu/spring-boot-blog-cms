package com.oyku.blog.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Comment {
		
	private String id;
	private String commenterName;
	private String content;
	private LocalDateTime createdAt = LocalDateTime.now();
}
