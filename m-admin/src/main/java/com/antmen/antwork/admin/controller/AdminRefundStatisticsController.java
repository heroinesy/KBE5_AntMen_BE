package com.antmen.antwork.admin.controller;

import com.antmen.antwork.admin.api.AdminCustomerRefundStatisticsDto;
import com.antmen.antwork.admin.api.AdminManagerRefundStatisticsDto;
import com.antmen.antwork.admin.api.AdminRefundReasonStatisticsDto;
import com.antmen.antwork.admin.api.AdminRefundStatisticsSummaryDto;
import com.antmen.antwork.admin.service.AdminRefundStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/statistics/refunds")
public class AdminRefundStatisticsController {
    private final AdminRefundStatisticsService adminRefundStatisticsService;

    @GetMapping
    public ResponseEntity<AdminRefundStatisticsSummaryDto> getRefundStatistics() {
        return ResponseEntity.ok(adminRefundStatisticsService.getRefundSummary());
    }

    @GetMapping("/reasons")
    public ResponseEntity<List<AdminRefundReasonStatisticsDto>> getRefundReasonStats() {
        List<AdminRefundReasonStatisticsDto> result = adminRefundStatisticsService.getRefundReasonStatistics();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/customers/top")
    public ResponseEntity<List<AdminCustomerRefundStatisticsDto>> getTopRefundCustomers() {
        List<AdminCustomerRefundStatisticsDto> result = adminRefundStatisticsService.getTopApprovedRefundCustomers();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/managers/top")
    public ResponseEntity<List<AdminManagerRefundStatisticsDto>> getTopRefundManagers() {
        List<AdminManagerRefundStatisticsDto> result = adminRefundStatisticsService.getTopApprovedRefundManagers();
        return ResponseEntity.ok(result);
    }
}