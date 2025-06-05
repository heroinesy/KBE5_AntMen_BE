package com.antmen.antwork.common.api.request.reservation;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class CategoryRequestDto {
    private String categoryName;
    private Long categoryPrice;
    private Short categoryTime;
} 