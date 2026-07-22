package com.oyku.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oyku.blog.dto.request.category.CreateCategoryRequestDto;
import com.oyku.blog.dto.request.category.UpdateCategoryRequestDto;
import com.oyku.blog.dto.response.category.CategoryResponseDto;
import com.oyku.blog.service.CategoryService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
		return ResponseEntity.ok(categoryService.getAllCategories());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CreateCategoryRequestDto request) {

		CategoryResponseDto category = categoryService.createCategory(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(category);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {

		CategoryResponseDto category = categoryService.getCategoryById(id);

		return ResponseEntity.ok(category);
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id,
			@Valid @RequestBody UpdateCategoryRequestDto updateCategoryRequestDto) {

		CategoryResponseDto updatedCategory = categoryService.updateCategory(id, updateCategoryRequestDto);
		return ResponseEntity.ok(updatedCategory);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}

}
