package com.antmen.antwork.domain.category.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class CategoryOptionRequestDto {
    private Long categoryId;
    private String coName;
    private Integer coPrice;
    private Short coTime;
} 