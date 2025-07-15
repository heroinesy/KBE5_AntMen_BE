package com.antmen.antwork.admin.controller;

import com.antmen.antwork.admin.api.AdminReservationStatisticsDto;
import com.antmen.antwork.admin.service.AdminReservationStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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