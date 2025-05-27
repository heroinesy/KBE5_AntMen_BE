package com.antmen.antwork.common.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class CategoryResponseDto {
    private Long categoryId;
    private String categoryName;
    private Long categoryPrice;
    private Short categoryTime;
} 