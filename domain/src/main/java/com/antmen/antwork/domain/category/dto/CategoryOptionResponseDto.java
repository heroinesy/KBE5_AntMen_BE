package com.antmen.antwork.domain.category.dto;

import com.antmen.antwork.common.domain.entity.reservation.CategoryOption;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class CategoryOptionResponseDto {
    private Long categoryId;
    private Long coId;
    private String coName;
    private Integer coPrice;
    private Short coTime;

    public static CategoryOptionResponseDto fromEntity(CategoryOption option) {
        return CategoryOptionResponseDto.builder()
                .categoryId(option.getCategory().getCategoryId())
                .coId(option.getCoId())
                .coName(option.getCoName())
                .coPrice(option.getCoPrice())
                .coTime(option.getCoTime())
                .build();
    }
}