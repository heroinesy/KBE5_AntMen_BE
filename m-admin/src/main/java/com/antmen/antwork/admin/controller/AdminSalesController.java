package com.antmen.antwork.admin.controller;

import com.antmen.antwork.admin.api.AdminSalesSummaryResponseDto;
import com.antmen.antwork.admin.service.AdminSalesStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/sales")
public class AdminSalesController {
    private final AdminSalesStatisticService adminSalesStatisticService;

    @GetMapping("/sales-summary")
    public ResponseEntity<AdminSalesSummaryResponseDto> getSalesStatistic(){
        return ResponseEntity.ok(adminSalesStatisticService.getSummarySales());
    }
}
