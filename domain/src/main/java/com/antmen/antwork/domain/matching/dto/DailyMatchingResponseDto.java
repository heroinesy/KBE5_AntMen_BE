package com.antmen.antwork.domain.matching.dto;









@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyMatchingResponseDto {
    private LocalDate date;
    private Long requestCount;
    private Long successCount;
    private BigDecimal matchingRate;
}
