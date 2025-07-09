package com.antmen.antwork.admin.service;

import com.antmen.antwork.admin.api.AdminSalesSummaryResponseDto;
import com.antmen.antwork.common.infra.repository.reservation.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminSalesStatisticService {
    private final PaymentRepository paymentRepository;

    public AdminSalesSummaryResponseDto getSummarySales() {
        Long total = Optional.ofNullable(paymentRepository.findSum()).orElse(0L);
        Long month = Optional.ofNullable(paymentRepository.findMonth()).orElse(0L);
        Long days = Optional.ofNullable(paymentRepository.findPaymentDays()).orElse(0L);

        Long dailyAverage = total / days;

        return AdminSalesSummaryResponseDto.builder()
                .totalSales(total)
                .currentMonthSales(month)
                .averageDailySales(dailyAverage).build();
    }
}
