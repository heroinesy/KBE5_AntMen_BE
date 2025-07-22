package com.antmen.antwork.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyInquiryRefundResponseDto {
    private LocalDate date;
    private Long dailyCustomerInquiries;
    private Long dailyManagerInquiries;
    private Long dailyRefunds;
} 