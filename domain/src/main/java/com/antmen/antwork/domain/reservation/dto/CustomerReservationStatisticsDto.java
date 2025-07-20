package com.antmen.antwork.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerReservationStatisticsDto {
    private Long totalReservations;      // 총 예약 건수
    private Long completedReservations;  // 완료된 예약 건수
    private Long pendingReservations;    // 진행 예정 건수
    private Long refundRequests;         // 환불 신청 건수
    private Long cancelledReservations;  // 취소된 예약 건수
    private Double successRate;          // 성공률 (%)
    private Double averageSatisfaction;  // 평균 만족도 (1-5)
} 