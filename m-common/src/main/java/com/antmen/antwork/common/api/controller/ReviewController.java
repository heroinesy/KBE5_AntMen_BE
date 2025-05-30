package com.antmen.antwork.common.api.controller;

import com.antmen.antwork.common.api.request.ReviewRequestDto;
import com.antmen.antwork.common.api.response.ReviewResponseDto;
import com.antmen.antwork.common.service.ReviewService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ReviewResponseDto> createReview(
            @Valid
            @RequestBody
            ReviewRequestDto dto
    ) {
        Long loginId = 3L;
        return ResponseEntity.ok(reviewService.createReview(loginId, dto));
    }

    // 리뷰 단건 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReview(
            @PathVariable
            Long reviewId
    ) {
        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }

    // 리뷰 전체 조회
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    // 내가 받은 리뷰 목록 조회
    @GetMapping("/received")
    public ResponseEntity<List<ReviewResponseDto>> getMyReceivedReviews(){
        Long loginId = 3L;
        return ResponseEntity.ok(reviewService.getMyReceivedReviews(loginId));
    }

    // 내가 쓴 리뷰 목록 조회
    @GetMapping("/written")
    public ResponseEntity<List<ReviewResponseDto>> getMyWrittenReviews() {
        Long loginId = 3L;
        return ResponseEntity.ok(reviewService.getMyWrittenReviews(loginId));
    }

    // 특정 id의 리뷰 목록 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByUserId(
            @PathVariable
            Long userId
    ) {
       return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable
            Long reviewId,
            @Valid
            @RequestBody
            ReviewRequestDto dto
    ) {
        Long loginId = 3L;
        return ResponseEntity.ok(reviewService.updateReview(loginId, reviewId, dto));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable
            Long reviewId
    ) {
        Long loginId = 3L;
        reviewService.deleteReview(loginId, reviewId);
        return ResponseEntity.ok().build();
    }

}
