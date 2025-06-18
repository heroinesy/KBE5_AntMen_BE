package com.antmen.antwork.manager.controller;

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
@RequestMapping("/v1/manager/reviews")
public class ManagerReviewController {
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

    // 내가 받은 리뷰 목록 조회 (매니저)
    @GetMapping("/my/received")
    public ResponseEntity<List<ReviewResponseDto>> getMyReceivedReviews(
            @AuthenticationPrincipal AuthUserDto authUserDto) {
        Long loginId = authUserDto.getUserIdAsLong();
        return ResponseEntity.ok(reviewService.getMyReceivedReviews(loginId));
    }

    // 내가 쓴 리뷰 목록 조회
    @GetMapping("/my/written")
    public ResponseEntity<List<ReviewResponseDto>> getMyWrittenReviews(
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        Long loginId = authUserDto.getUserIdAsLong();
        return ResponseEntity.ok(reviewService.getMyWrittenReviews(loginId));
    }

    // 예약번호로 리뷰작성여부 판단
//    @GetMapping("/exists")
//    public ResponseEntity<Boolean> existsReview(
//            @AuthenticationPrincipal AuthUserDto authUserDto,
//            @RequestParam Long reservationId
//    ) {
//        return ResponseEntity.ok(reviewService.existsByReservationIdAndAuthorId(reservationId, authUserDto.getUserIdAsLong()));
//    }

}
