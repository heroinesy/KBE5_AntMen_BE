package com.antmen.antwork.domain.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryOptionRequestDto {
    private Long categoryId;
    private String coName;
    private Integer coPrice;
    private Short coTime;
} 