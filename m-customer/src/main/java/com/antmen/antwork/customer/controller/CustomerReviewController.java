package com.antmen.antwork.customer.controller;

import com.antmen.antwork.common.api.request.reservation.ReviewRequestDto;
import com.antmen.antwork.common.api.response.reservation.ReviewResponseDto;
import com.antmen.antwork.common.service.serviceReservation.ReviewService;
import com.antmen.antwork.common.util.AuthUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/reviews")
public class CustomerReviewController {
    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @Valid
            @RequestBody
            ReviewRequestDto dto
    ) {
        Long loginId = authUserDto.getUserIdAsLong();
        return ResponseEntity.ok(reviewService.createReview(loginId, dto));
    }

    // 내가 쓴 리뷰 목록 조회
    @GetMapping("/my/written")
    public ResponseEntity<List<ReviewResponseDto>> getMyWrittenReviews(
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        Long loginId = authUserDto.getUserIdAsLong();
        return ResponseEntity.ok(reviewService.getMyWrittenReviews(loginId));
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @PathVariable
            Long reviewId,
            @Valid
            @RequestBody
            ReviewRequestDto dto
    ) {
        Long loginId = authUserDto.getUserIdAsLong();
        return ResponseEntity.ok(reviewService.updateReview(loginId, reviewId, dto));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @PathVariable
            Long reviewId
    ) {
        Long loginId = authUserDto.getUserIdAsLong();
        reviewService.deleteReview(loginId, reviewId);
        return ResponseEntity.ok().build();
    }

}
