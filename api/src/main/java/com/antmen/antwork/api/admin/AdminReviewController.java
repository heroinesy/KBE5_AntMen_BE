package com.antmen.antwork.api.admin;












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
