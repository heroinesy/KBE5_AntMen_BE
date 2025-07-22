package com.antmen.antwork.domain.reservation.dto;








@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationSummaryResponseDto {
    private Long totalCount;
    private Long cancelCount;
    private Long completeCount;
    private BigDecimal cancelRate;
    private BigDecimal avgUser;
}