package com.antmen.antwork.domain.review.dto;

import com.antmen.antwork.common.domain.entity.ReviewSummary;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSummaryResponseDto {
    private Long totalReviews;
    private BigDecimal avgRating;

    public static ReviewSummaryResponseDto from(ReviewSummary reviewSummary) {
        if (reviewSummary == null) {return ReviewSummaryResponseDto.defaultSummary();}

        return ReviewSummaryResponseDto.builder()
                .totalReviews(reviewSummary.getTotalReviews())
                .avgRating(reviewSummary.getAvgRating())
                .build();
    }

    public static ReviewSummaryResponseDto defaultSummary() {
        return ReviewSummaryResponseDto.builder()
                .totalReviews(0L)
                .avgRating(BigDecimal.ZERO)
                .build();
    }
}