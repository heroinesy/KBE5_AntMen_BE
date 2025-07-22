package com.antmen.antwork.domain.review.dto;









@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminReviewStatisticsDto {
    private Long totalReviewCount;
    private BigDecimal avgReviewSatisfaction;
    private BigDecimal avgCustomerReviewSatisfaction;
    private BigDecimal avgManagerReviewSatisfaction;

    private List<ReviewSatisfactionDto> topCustomerList;
    private List<ReviewSatisfactionDto> topManagerList;
    private List<ReviewSatisfactionDto> topCustomerByReviewCount;
    private List<ReviewSatisfactionDto> topManagerByReviewCount;
}