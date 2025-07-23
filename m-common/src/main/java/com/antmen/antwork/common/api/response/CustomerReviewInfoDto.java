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
public class CustomerReviewInfoDto {
    private List<CustomerReviewDto> writtenReviews;  // 작성한 리뷰 목록
    private List<CustomerReviewDto> receivedReviews; // 받은 리뷰 목록
    private Long totalWrittenReviews;                // 작성한 리뷰 총 개수
    private Long totalReceivedReviews;               // 받은 리뷰 총 개수
    private Double averageWrittenRating;             // 작성한 리뷰 평균 평점
    private Double averageReceivedRating;            // 받은 리뷰 평균 평점
} 