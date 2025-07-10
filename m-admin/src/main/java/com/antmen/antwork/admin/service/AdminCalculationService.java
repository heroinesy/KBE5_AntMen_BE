package com.antmen.antwork.admin.service;

import com.antmen.antwork.admin.api.AdminMonthCalculationResponseDto;
import com.antmen.antwork.admin.api.AdminSummaryCalculationResponseDto;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.reservation.Calculation;
import com.antmen.antwork.common.infra.repository.reservation.CalculationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminCalculationService {
    private final CalculationRepository calculationRepository;

    public AdminSummaryCalculationResponseDto getCalculationSummary(){
        Long total = Optional.ofNullable(calculationRepository.findSum()).orElse(0L);
        Long month = Optional.ofNullable(calculationRepository.findMonth()).orElse(0L);
        int count = (int) calculationRepository.count();

        LocalDateTime oneMonthAgo = LocalDate.now().minusMonths(1).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        List<Calculation> recentList = calculationRepository.findByRequestedAtBetween(oneMonthAgo, now);

        List<AdminMonthCalculationResponseDto> recentMonthCalculations = recentList.stream()
                .map(cal -> {
                    User manager = cal.getManager();
                    return AdminMonthCalculationResponseDto.builder()
                            .calculationId(cal.getCalculationId())
                            .managerId(manager.getUserId())
                            .managerName(manager.getUserName())
                            .startDate(cal.getStartDate())
                            .endDate(cal.getEndDate())
                            .amount(cal.getAmount())
                            .requestedAt(cal.getRequestedAt())
                            .build();
                }).toList();

        return AdminSummaryCalculationResponseDto.builder()
                .totalAmount(total)
                .currentMonthAmount(month)
                .calculationCount(count)
                .recentMonthCalculations(recentMonthCalculations).build();
    }
}