package com.oyku.blog.dto.request.post;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequestDto {

    @NotBlank(message = "Title can't be empty.")
    @Size(max = 100, message = "Title must be maximum of 500 characters.")
    private String title;

    @NotBlank(message = "Summary can't be empty.")
    @Size(max = 400, message = "Summary must be maximum of 400 characters.")
    private String summary;

    @NotBlank(message = "Content can't be empty.")
    @Size(max = 5000, message = "Content must be maximum of 5000 characters.")
    private String content;

    @NotBlank(message = "Author name can't be empty.")
    @Size(max = 60, message = "Author name must be maximum of 60 characters.")
    private String authorName;

    @NotEmpty(message = "Tag can not be empty.")
    @Size(max = 6, message = "A maximum of 6 tags can be entered.")
    private List<
            @NotBlank(message = "Tag can't be empty.")
            @Size(max = 30, message = "Tag must be maximum of 30 characters.")
            String
    > tags;

    @NotNull(message = "Category can't be empty.")
    private Long categoryId;
    }