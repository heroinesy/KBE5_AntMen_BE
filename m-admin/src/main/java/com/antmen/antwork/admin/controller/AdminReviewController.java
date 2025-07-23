package com.antmen.antwork.admin.controller;

import com.antmen.antwork.common.api.response.reservation.ReviewResponseDto;
import com.antmen.antwork.common.service.serviceReservation.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/reviews")
public class AdminReviewController {
    private final ReviewService reviewService;

    // 특정 유저에게 달린 리뷰 조회 (관리자용)
    @GetMapping("/received/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getReceivedReviews(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }

    // 전체 리뷰 조회 (관리자용)
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }
}
