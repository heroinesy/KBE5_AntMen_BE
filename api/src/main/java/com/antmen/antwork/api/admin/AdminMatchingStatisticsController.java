package com.antmen.antwork.api.admin;









@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/statistics/matchings")
public class AdminMatchingStatisticsController {
    private final AdminMatchingStatisticsService adminMatchingStatisticsService;

    @GetMapping
    public ResponseEntity<AdminMatchingStatisticsSummaryDto> getMatchingStatistics(){
        return ResponseEntity.ok(adminMatchingStatisticsService.getMatchingStatisticsSummaryDto());
    }
}