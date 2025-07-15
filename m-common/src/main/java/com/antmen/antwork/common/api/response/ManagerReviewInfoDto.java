package com.antmen.antwork.common.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerReviewInfoDto {
    private Integer totalWrittenReviews;
    private Integer totalReceivedReviews;
    private Double averageWrittenRating;
    private Double averageReceivedRating;
    private List<CustomerReviewDto> writtenReviews;
    private List<CustomerReviewDto> receivedReviews;
} 