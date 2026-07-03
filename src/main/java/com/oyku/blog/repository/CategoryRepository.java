package com.oyku.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oyku.blog.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
