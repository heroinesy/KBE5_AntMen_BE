package com.antmen.antwork.domain.review.dto;








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