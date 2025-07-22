package com.antmen.antwork.domain.review.dto;









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