package com.antmen.antwork.common.api.controller;

import com.antmen.antwork.common.api.request.ReviewRequestDto;
import com.antmen.antwork.common.api.response.ReviewResponseDto;
import com.antmen.antwork.common.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewRequestDto dto) {
        Long loginId = 3L;
        return ResponseEntity.ok(reviewService.createReview(loginId, dto));
    }

    // 리뷰 단건 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }

    // 리뷰 전체 조회
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequestDto dto) {
        Long loginId = 3L;
        return ResponseEntity.ok(reviewService.updateReview(loginId, reviewId, dto));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        Long loginId = 3L;
        reviewService.deleteReview(loginId, reviewId);
        return ResponseEntity.ok().build();
    }

}
