package com.antmen.antwork.api.admin;










@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/statistics/reviews")
public class AdminReviewStatisticsController {
    private final AdminReviewStatisticService adminReviewStatisticService;

    @GetMapping
    public ResponseEntity<AdminReviewStatisticsDto> getReviewStatistics(@RequestParam(defaultValue = "3") int topN) {
        return ResponseEntity.ok(adminReviewStatisticService.getReviewStatistics(topN));
    }
}