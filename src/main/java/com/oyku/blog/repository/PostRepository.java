package com.oyku.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.oyku.blog.dto.response.statistics.AuthorStatisticsResponseDto;
import com.oyku.blog.dto.response.statistics.CategoryStatisticsResponseDto;
import com.oyku.blog.dto.response.statistics.StatusStatisticsResponseDto;
import com.oyku.blog.entity.Post;

public interface PostRepository extends JpaRepository<Post, String>, JpaSpecificationExecutor<Post>{
	
	@Query("""
			SELECT new com.oyku.blog.dto.response.statistics.AuthorStatisticsResponseDto(
			p.authorName,
			COUNT(p)
			)
			FROM Post p
			GROUP BY p.authorName
			ORDER BY COUNT(p) desc
			""")
	List<AuthorStatisticsResponseDto> getAuthorStatistics();

	@Query("""
			SELECT new com.oyku.blog.dto.response.statistics.StatusStatisticsResponseDto(
			s.status,
			COUNT(s)
			)
			FROM Post s
			GROUP BY s.status
			""")
	List<StatusStatisticsResponseDto> getStatusStatistics();

	@Query("""
			SELECT new com.oyku.blog.dto.response.statistics.CategoryStatisticsResponseDto(
			c.name,
			COUNT(p)
			)
			FROM Post p
			JOIN p.category c
			GROUP BY c.name
			ORDER BY Count(p) desc
			""")
	List<CategoryStatisticsResponseDto> getCategoryStatistics();

	boolean existsBySlug(String slug);
	

}
