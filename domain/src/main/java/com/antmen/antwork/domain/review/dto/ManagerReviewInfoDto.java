package com.antmen.antwork.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerReviewInfoDto {
    private Integer totalWrittenReviews;
    private Integer totalReceivedReviews;
    private BigDecimal averageWrittenRating;
    private BigDecimal averageReceivedRating;
    private List<CustomerReviewDto> writtenReviews;
    private List<CustomerReviewDto> receivedReviews;
} 