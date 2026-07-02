package com.oyku.blog.dto.request;

import java.util.List;

import com.oyku.blog.enums.Category;

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

    @NotBlank(message = "Title boş olamaz.")
    @Size(max = 100, message = "Title en fazla 100 karakter olmalıdır.")
    private String title;

    @NotBlank(message = "Summary boş olamaz.")
    @Size(max = 400, message = "Summary en fazla 400 karakter olmalıdır.")
    private String summary;

    @NotBlank(message = "Content boş olamaz.")
    @Size(max = 5000, message = "Content en fazla 5000 karakter olmalıdır.")
    private String content;

    @NotBlank(message = "Author name boş olamaz.")
    @Size(max = 60, message = "Author name en fazla 60 karakter olmalıdır.")
    private String authorName;

    @NotEmpty(message = "En az bir tag girilmelidir.")
    @Size(max = 6, message = "En fazla 6 tag girilebilir.")
    private List<
            @NotBlank(message = "Tag boş olamaz.")
            @Size(max = 30, message = "Tag en fazla 30 karakter olabilir.")
            String
    > tags;

    @NotNull(message = "Category boş olamaz.")
    private Category category;
}