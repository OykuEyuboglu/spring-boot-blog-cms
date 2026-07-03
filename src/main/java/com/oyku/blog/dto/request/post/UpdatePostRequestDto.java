package com.oyku.blog.dto.request.post;

import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequestDto {

    @Size(max = 100, message = "Title must be maximum of 500 characters.")
    private String title;

    @Size(max = 400, message = "Summary must be maximum of 400 characters.")
    private String summary;

    @Size(max = 5000, message = "Content must be maximum of 5000 characters.")
    private String content;

    @Size(max = 60, message = "Author name must be maximum of 60 characters.")
    private String authorName;

    @Size(max = 6, message = "A maximum of 6 tags can be entered.")
    private List<
            @Size(max = 30, message = "Tag must be maximum of 30 characters.")
            String
    > tags;

    private  Long categoryId;
}