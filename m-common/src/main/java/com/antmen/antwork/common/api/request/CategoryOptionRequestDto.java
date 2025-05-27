package com.antmen.antwork.common.api.request;

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