package com.antmen.antwork.domain.payment.dto;








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