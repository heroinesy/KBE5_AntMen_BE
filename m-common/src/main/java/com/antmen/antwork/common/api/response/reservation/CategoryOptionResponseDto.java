package com.antmen.antwork.common.api.response.reservation;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class CategoryOptionResponseDto {
    private Long coId;
    private String coName;
    private Integer coPrice;
    private Short coTime;
} 