package com.antmen.antwork.manager.controller;

import com.antmen.antwork.common.api.response.reservation.ReviewResponseDto;
import com.antmen.antwork.common.service.serviceReservation.ReviewService;
import com.antmen.antwork.common.util.AuthUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manager/reviews")
public class ManagerReviewController {
    private final ReviewService reviewService;

    // 내가 받은 리뷰 목록 조회 (매니저)
    @GetMapping("/my/received")
    public ResponseEntity<List<ReviewResponseDto>> getMyReceivedReviews(
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        Long loginId = authUserDto.getUserIdAsLong();
        return ResponseEntity.ok(reviewService.getMyReceivedReviews(loginId));
    }
}
