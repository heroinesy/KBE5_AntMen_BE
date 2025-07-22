package com.antmen.antwork.api.admin;

import com.antmen.antwork.domain.payment.dto.AdminSalesSummaryResponseDto;
import com.antmen.antwork.domain.payment.service.AdminSalesStatisticService;
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

    @GetMapping
    public ResponseEntity<AdminSalesSummaryResponseDto> getSalesSummary(){
        return ResponseEntity.ok(adminSalesStatisticService.getFinalSalesProfit());
    }
}