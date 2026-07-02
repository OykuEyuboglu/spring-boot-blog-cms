package com.oyku.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oyku.blog.entity.Post;

public interface PostRepository extends JpaRepository<Post, String>{
	
}
