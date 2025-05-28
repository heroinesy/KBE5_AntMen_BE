package com.antmen.antwork.common.api.controller;

import com.antmen.antwork.common.api.request.ReviewRequest;
import com.antmen.antwork.common.api.response.ReviewResponse;
import com.antmen.antwork.common.domain.entity.Review;
import com.antmen.antwork.common.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // login_id로 수정 필요
    @PostMapping("/write")
    public ResponseEntity<ReviewResponse> writeReview(
            @RequestBody
            ReviewRequest reviewRequest
    ) {

        return ResponseEntity.ok().body(reviewService.writeReview(2L, reviewRequest));
    }



}
