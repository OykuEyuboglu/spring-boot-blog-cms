package com.oyku.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.oyku.blog.entity.Post;

public interface PostRepository extends JpaRepository<Post, String>, JpaSpecificationExecutor<Post>{
	
}
