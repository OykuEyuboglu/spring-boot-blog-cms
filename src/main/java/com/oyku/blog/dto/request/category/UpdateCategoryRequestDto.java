package com.oyku.blog.dto.request.category;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryRequestDto {

    @Size(max = 25, message = "Name must be maximum of 25 characters.")
    private String name;

    @Size(max = 150, message = "Description must be maximum of 150 characters.")
    private String description;
    
}
