package com.antmen.antwork.api.admin;

import com.antmen.antwork.domain.review.dto.AdminReviewStatisticsDto;
import com.antmen.antwork.domain.review.service.AdminReviewStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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