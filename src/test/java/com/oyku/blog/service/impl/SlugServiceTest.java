package com.oyku.blog.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oyku.blog.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class SlugServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private SlugService slugService;

    @Test
    void shouldGenerateSlugSuccessfully() {

        String title = "Hello World";

        when(postRepository.existsBySlug("hello-world")).thenReturn(false);

        String result = slugService.generateSlug(title);

        assertEquals("hello-world", result);

        verify(postRepository).existsBySlug("hello-world");
    }

    @Test
    void shouldGenerateUniqueSlugSuccessfully() {

        String title = "Hello World";

        when(postRepository.existsBySlug("hello-world")).thenReturn(true);
        when(postRepository.existsBySlug("hello-world-1")).thenReturn(false);

        String result = slugService.generateSlug(title);

        assertEquals("hello-world-1", result);

        verify(postRepository).existsBySlug("hello-world");
        verify(postRepository).existsBySlug("hello-world-1");
    }

    @Test
    void shouldGenerateSlugWithTurkishCharacters() {

        String title = "Örnek";

        when(postRepository.existsBySlug("ornek")).thenReturn(false);

        String result = slugService.generateSlug(title);

        assertEquals("ornek", result);

        verify(postRepository).existsBySlug("ornek");
    }

    @Test
    void shouldRemoveSpecialCharactersFromSlug() {

        String title = "Java!!! @@ Spring ###";

        when(postRepository.existsBySlug("java-spring")).thenReturn(false);

        String result = slugService.generateSlug(title);

        assertEquals("java-spring", result);

        verify(postRepository, times(1)).existsBySlug("java-spring");
    }
}