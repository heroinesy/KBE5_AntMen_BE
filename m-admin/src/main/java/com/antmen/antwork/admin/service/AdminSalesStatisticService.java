package com.antmen.antwork.admin.service;

import com.antmen.antwork.admin.api.AdminDailySaleResponseDto;
import com.antmen.antwork.admin.api.AdminSalesSummaryResponseDto;
import com.antmen.antwork.common.domain.entity.reservation.Payment;
import com.antmen.antwork.common.domain.entity.reservation.PaymentStatus;
import com.antmen.antwork.common.infra.repository.reservation.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminSalesStatisticService {
    private final PaymentRepository paymentRepository;

    public AdminSalesSummaryResponseDto getFinalSalesProfit() {
        Long total = Optional.ofNullable(paymentRepository.findSum()).orElse(0L);
        Long month = Optional.ofNullable(paymentRepository.findMonth()).orElse(0L);
        Long days = Optional.ofNullable(paymentRepository.findPaymentDays()).orElse(0L);
        Long dailyAverage = total / days;

        Long totalProfit = Math.round(total * 0.9);
        Long monthProfit = Math.round(month * 0.9);
        Long daysProfit = totalProfit / days;

        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);

        List<Payment> payments = paymentRepository.findAllByPayCreatedTimeBetweenAndPayStatus(
                start.atStartOfDay(),
                today.atTime(LocalTime.MAX),
                PaymentStatus.DONE);

        Map<LocalDate, Long> SalesMap = payments.stream()
                .collect(Collectors.groupingBy
                        (p-> p.getPayCreatedTime().toLocalDate(),
                                Collectors.summingLong(Payment::getPayAmount)));

        List<AdminDailySaleResponseDto> dailySalesProfitList = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> {
                    LocalDate dailyDate = start.plusDays(i);
                    Long dailySales = SalesMap.getOrDefault(dailyDate, 0L);
                    Long dailyProfit = Math.round(dailySales * 0.9);
                    return AdminDailySaleResponseDto.builder()
                            .dailyDate(dailyDate)
                            .dailySales(dailySales)
                            .dailyFee(dailySales - dailyProfit)
                            .dailyProfit(dailyProfit)
                            .build();
                }).collect(Collectors.toList());

        return AdminSalesSummaryResponseDto.builder()
                .totalSales(total)
                .currentMonthSales(month)
                .averageDailySales(dailyAverage)
                .totalProfit(totalProfit)
                .currentMonthProfit(monthProfit)
                .averageDailyProfit(daysProfit)
                .recentWeeklySalesProfit(dailySalesProfitList).build();
    }
}