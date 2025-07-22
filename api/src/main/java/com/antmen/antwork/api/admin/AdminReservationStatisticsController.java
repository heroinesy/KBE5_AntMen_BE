package com.antmen.antwork.api.admin;










@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/statistics/reservations")
public class AdminReservationStatisticsController {
    private final AdminReservationStatisticsService adminReservationStatisticsService;

    @GetMapping
    public ResponseEntity<AdminReservationStatisticsDto> getReviewStatistics(@RequestParam(defaultValue = "7") int recentDays) {
        return ResponseEntity.ok(adminReservationStatisticsService.getReservationStatistics(recentDays));
    }
}