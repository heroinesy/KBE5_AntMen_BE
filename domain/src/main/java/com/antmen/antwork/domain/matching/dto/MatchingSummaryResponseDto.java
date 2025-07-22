package com.antmen.antwork.domain.matching.dto;








@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchingSummaryResponseDto {
    private BigDecimal matchingRating;
    private Long totalMatchingCount;
    private Long successCount;
    private Long failCount;

    private BigDecimal customerRefuseRate;
    private BigDecimal managerRefuseRate;
}
