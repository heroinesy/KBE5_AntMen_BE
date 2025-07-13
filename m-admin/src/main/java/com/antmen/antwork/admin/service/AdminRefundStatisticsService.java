package com.antmen.antwork.admin.service;

import com.antmen.antwork.admin.api.AdminRefundStatisticsSummaryDto;
import com.antmen.antwork.common.domain.entity.reservation.RefundStatus;
import com.antmen.antwork.common.infra.repository.reservation.RefundRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminRefundStatisticsService {
    private final RefundRepository refundRepository;
    private final ReservationRepository reservationRepository;

    public AdminRefundStatisticsSummaryDto getRefundSummary(){
        long totalRefundCount = refundRepository.count();
        Long approveRefundCount = refundRepository.countByRefundStatus(RefundStatus.APPROVED);
        Long approveRefundAmount = refundRepository.TotalRefundAmountByStatus(RefundStatus.APPROVED);
        long totalReservationCount = reservationRepository.count();

        double rawRate = (double) totalRefundCount / totalReservationCount * 100;
        double refundRate = Math.round(rawRate * 100.0) / 100.0;

        return AdminRefundStatisticsSummaryDto.builder()
                .refundRate(refundRate)
                .approveRefundCount(approveRefundCount)
                .totalRefundCount(totalRefundCount)
                .totalRefundAmount(approveRefundAmount)
                .build();
    }
}