package com.emergency.material.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CategoryDto {
    @JsonProperty("category_code")
    private String categoryCode;
    
    @JsonProperty("category_name")
    private String categoryName;
}
