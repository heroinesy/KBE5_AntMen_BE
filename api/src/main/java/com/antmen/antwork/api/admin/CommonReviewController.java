package com.antmen.antwork.api.admin;









@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common/reviews")
public class CommonReviewController {
    private final ReviewService reviewService;

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

    // 특정 id의 리뷰 목록 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByUserId(
            @PathVariable
            Long userId
    ) {
       return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }
}
