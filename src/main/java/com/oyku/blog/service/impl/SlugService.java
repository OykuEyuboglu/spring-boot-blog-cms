package com.oyku.blog.service.impl;

import java.text.Normalizer;

import org.springframework.stereotype.Service;

import com.oyku.blog.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlugService {
	
	private final PostRepository postRepository;
	
	public String generateSlug(String title) {
		
		String baseSlug = toSlug(title);
	
		String slug = baseSlug;
		
		int counter = 1;
		
		while (postRepository.existsBySlug(slug)) {
			slug = baseSlug + "-" + counter;
			counter++;
		}
		
		return slug;
	}

	
	 private String toSlug(String text) {

	        String slug = Normalizer.normalize(text, Normalizer.Form.NFD)
	                .replaceAll("\\p{M}", "");

	        slug = slug.toLowerCase();

	        slug = slug.replaceAll("[^a-z0-9\\s-]", "");

	        slug = slug.trim();

	        slug = slug.replaceAll("\\s+", "-");

	        slug = slug.replaceAll("-+", "-");

	        return slug;
	    }
}
