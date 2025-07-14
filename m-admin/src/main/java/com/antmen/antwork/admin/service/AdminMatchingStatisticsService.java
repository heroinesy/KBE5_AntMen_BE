package com.antmen.antwork.admin.service;

import com.antmen.antwork.admin.api.AdminMatchingStatisticsSummaryDto;
import com.antmen.antwork.admin.api.DailyMatchingResponseDto;
import com.antmen.antwork.admin.api.MatchingSummaryResponseDto;
import com.antmen.antwork.admin.api.MatchingTopManagerDto;
import com.antmen.antwork.common.infra.repository.reservation.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMatchingStatisticsService {
    private final MatchingRepository matchingRepository;

    public AdminMatchingStatisticsSummaryDto getMatchingStatisticsSummaryDto() {
        long total = matchingRepository.count();
        Long success = matchingRepository.countSuccess();
        Long fail = total - success;

        BigDecimal matchingRate = total == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(success)
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        Long customerRefuse = matchingRepository.countRefusedByCustomer();
        Long managerRefuse = matchingRepository.countRefusedByManager();

        BigDecimal customerRefuseRate = (total == 0)
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(customerRefuse)
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        BigDecimal managerRefuseRate = (total == 0)
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(managerRefuse)
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        List<MatchingTopManagerDto> topManagers = matchingRepository.findTopManagers(Pageable.ofSize(5)).stream()
                .map(p-> MatchingTopManagerDto.builder()
                        .managerId(p.getManagerId())
                        .managerName(p.getManagerName())
                        .successCount(p.getSuccessCount())
                        .build()).toList();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);
        List<DailyMatchingResponseDto> dailyList = matchingRepository.findDailyMatchingStats(
                startDate.atStartOfDay(), today.atTime(23,59,59)).stream()
                .map(p-> {
            BigDecimal rate = p.getRequestCount() == 0 ? BigDecimal.ZERO :
                    BigDecimal.valueOf(p.getSuccessCount())
                            .divide(BigDecimal.valueOf(p.getRequestCount()), 2, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));
            return DailyMatchingResponseDto.builder()
                    .date(p.getDate())
                    .requestCount(p.getRequestCount())
                    .successCount(p.getSuccessCount())
                    .matchingRate(rate).build();
        }).toList();

        MatchingSummaryResponseDto summary = MatchingSummaryResponseDto.builder()
                .matchingRating(matchingRate)
                .totalMatchingCount(total)
                .successCount(success)
                .failCount(fail)
                .customerRefuseRate(customerRefuseRate)
                .managerRefuseRate(managerRefuseRate)
                .build();

        return AdminMatchingStatisticsSummaryDto.builder()
                .matchingSummary(summary)
                .topManagerList(topManagers)
                .dailyMatchingList(dailyList)
                .build();
    }
}