package com.antmen.antwork.api.admin;












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