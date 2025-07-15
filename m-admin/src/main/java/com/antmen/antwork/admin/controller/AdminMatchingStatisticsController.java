package com.antmen.antwork.admin.controller;

import com.antmen.antwork.admin.api.AdminMatchingStatisticsSummaryDto;
import com.antmen.antwork.admin.service.AdminMatchingStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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