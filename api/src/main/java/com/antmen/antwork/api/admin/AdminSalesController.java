package com.antmen.antwork.api.admin;









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