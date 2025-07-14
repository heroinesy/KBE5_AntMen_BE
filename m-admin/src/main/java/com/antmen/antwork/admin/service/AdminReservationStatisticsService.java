package com.antmen.antwork.admin.service;

import com.antmen.antwork.admin.api.AdminReservationStatisticsDto;
import com.antmen.antwork.admin.api.DailyReservationResponseDto;
import com.antmen.antwork.admin.api.ReservationCategoryResponseDto;
import com.antmen.antwork.admin.api.ReservationSummaryResponseDto;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReservationStatisticsService {
    private final ReservationRepository reservationRepository;

    public AdminReservationStatisticsDto getReservationStatistics(int recentDays) {
        ReservationRepository.ReservationSummaryProjection summary = reservationRepository.getReservationSummary();

        Long totalCount = summary.getTotalCount();
        Long cancelCount = summary.getCancelCount();
        Long completeCount = summary.getCompleteCount();
        Long userCount = summary.getUserCount();

        totalCount = totalCount != null ? totalCount : 0L;
        cancelCount = cancelCount != null ? cancelCount : 0L;
        completeCount = completeCount != null ? completeCount : 0L;
        userCount = userCount != null ? userCount : 1L;

        BigDecimal cancelRate = totalCount > 0
                ? BigDecimal.valueOf(cancelCount).divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;
        BigDecimal avgUser = totalCount > 0
                ? BigDecimal.valueOf(totalCount).divide(BigDecimal.valueOf(userCount), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        ReservationSummaryResponseDto summaryDto = ReservationSummaryResponseDto.builder()
                .totalCount(totalCount)
                .cancelCount(cancelCount)
                .completeCount(completeCount)
                .cancelRate(cancelRate)
                .avgUser(avgUser)
                .build();

        LocalDate startDate = LocalDate.now().minusDays(recentDays - 1); // 오늘 포함
        List<DailyReservationResponseDto> dailyList = reservationRepository.getDailyReservations(startDate).stream()
                .map(d -> new DailyReservationResponseDto(
                        d.getDate(),
                        d.getDailyReservationsCount(),
                        d.getDailyCancelCount(),
                        d.getDailyCompletedCount()
                )).toList();

        List<ReservationCategoryResponseDto> categoryList = reservationRepository.getReservationCountByCategory().stream()
                .map(c -> new ReservationCategoryResponseDto(c.getCategoryName(), c.getCategoryCount()))
                .toList();

        return AdminReservationStatisticsDto.builder()
                .reservationSummary(summaryDto)
                .dailyList(dailyList)
                .categoryList(categoryList)
                .build();
    }
}