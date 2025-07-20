package com.antmen.antwork.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSatisfactionDto {
    private Long userId;
    private String userName;
    private BigDecimal avgReview;
    private Long totalReviewCount;
}