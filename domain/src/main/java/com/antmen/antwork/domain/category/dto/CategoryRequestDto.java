package com.antmen.antwork.domain.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryRequestDto {
    private String categoryName;
    private Long categoryPrice;
    private Short categoryTime;
} 