package com.antmen.antwork.common.api.response.reservation;

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
        return ReviewSummaryResponseDto.builder()
                .totalReviews(reviewSummary.getTotalReviews())
                .avgRating(reviewSummary.getAvgRating())
                .build();
    }
}