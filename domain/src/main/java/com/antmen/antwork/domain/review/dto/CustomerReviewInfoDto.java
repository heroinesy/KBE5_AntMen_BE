package com.antmen.antwork.domain.review.dto;








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