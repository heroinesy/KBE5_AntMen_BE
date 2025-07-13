package com.antmen.antwork.admin.service;

import com.antmen.antwork.admin.api.AdminCustomerRefundStatisticsDto;
import com.antmen.antwork.admin.api.AdminManagerRefundStatisticsDto;
import com.antmen.antwork.admin.api.AdminRefundReasonStatisticsDto;
import com.antmen.antwork.admin.api.AdminRefundStatisticsSummaryDto;
import com.antmen.antwork.common.domain.entity.reservation.RefundStatus;
import com.antmen.antwork.common.infra.repository.reservation.RefundRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<AdminRefundReasonStatisticsDto> getRefundReasonStatistics() {
        return refundRepository.CountByRefundReason().stream()
                .map(AdminRefundReasonStatisticsDto::from)
                .collect(Collectors.toList());
    }

    public List<AdminCustomerRefundStatisticsDto> getTopApprovedRefundCustomers() {
        return refundRepository.getTopApprovedRefundCustomers(RefundStatus.APPROVED).stream()
                .map(p -> new AdminCustomerRefundStatisticsDto(
                        p.getCustomerId(),
                        p.getCustomerName(),
                        p.getRefundCount(),
                        p.getTotalRefundAmount()
                ))
                .collect(Collectors.toList());
    }

    public List<AdminManagerRefundStatisticsDto> getTopApprovedRefundManagers() {
        return refundRepository.getTopApprovedRefundManagers(RefundStatus.APPROVED).stream()
                .map(p -> new AdminManagerRefundStatisticsDto(
                        p.getManagerId(),
                        p.getManagerName(),
                        p.getRefundCount(),
                        p.getTotalRefundAmount()
                ))
                .collect(Collectors.toList());
    }
}