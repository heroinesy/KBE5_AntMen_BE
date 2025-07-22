package com.antmen.antwork.api.admin;















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

    @GetMapping("/reasons/top")
    public ResponseEntity<List<AdminRefundReasonStatisticsDto>> getTopRefundReasons(
            @RequestParam(defaultValue = "5") int topCount) {
        List<AdminRefundReasonStatisticsDto> result = adminRefundStatisticsService.getTopRefundReasons(topCount);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/reasons/auto-refund-details")
    public ResponseEntity<List<AdminRefundReasonStatisticsDto>> getAutoRefundDetails() {
        List<AdminRefundReasonStatisticsDto> result = adminRefundStatisticsService.getAutoRefundDetails();
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