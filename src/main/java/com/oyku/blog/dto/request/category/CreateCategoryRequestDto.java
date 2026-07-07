package com.oyku.blog.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryRequestDto {

    @NotBlank(message = "Name can't be empty.")
    @Size(max = 25, message = "Name must be maximum of 25 characters.")
    private String name;

    @NotBlank(message = "Description can't be empty.")
    @Size(max = 150, message = "Description must be maximum of 150 characters.")
    private String description;
    
}
