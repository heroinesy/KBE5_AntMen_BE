package com.antmen.antwork.api.admin;

import com.antmen.antwork.domain.payment.dto.DailyInquiryRefundResponseDto;
import com.antmen.antwork.domain.payment.service.AdminInquiryRefundStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/statistics/inquiry-refund")
public class AdminInquiryRefundStatisticsController {
    private final AdminInquiryRefundStatisticsService adminInquiryRefundStatisticsService;

    @GetMapping
    public ResponseEntity<List<DailyInquiryRefundResponseDto>> getInquiryRefundStatistics(@RequestParam(defaultValue = "7") int recentDays) {
        return ResponseEntity.ok(adminInquiryRefundStatisticsService.getDailyInquiryRefundStatistics(recentDays));
    }
} 