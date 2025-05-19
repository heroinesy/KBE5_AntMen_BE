package com.antmen.antwork.customer.controller;
import com.antmen.antwork.customer.service.ReviewUserService;
import com.antmen.antwork.entity.ReviewUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews/users")
public class ReviewUserController {

    @Autowired
    private ReviewUserService reviewUserService;

    /**
     * 리뷰 등록
     */
    @PostMapping
    public ReviewUser createReviewUser(@RequestBody ReviewUser reviewUser) {
        return reviewUserService.createReviewUser(reviewUser);
    }

    /**
     * 전체 리뷰 조회
     */
    @GetMapping
    public List<ReviewUser> getAllReviewUsers() {
        return reviewUserService.getAllReviewUsers();
    }

    /**
     * 특정 리뷰 조회
     */
    @GetMapping("/{id}")
    public Optional<ReviewUser> getReviewUserById(@PathVariable int id) {
        return reviewUserService.getReviewUserById(id);
    }

    /**
     * 리뷰 수정
     */
    @PutMapping("/{id}")
    public ReviewUser updateReviewUser(@PathVariable int id, @RequestBody ReviewUser reviewUser) {
        return reviewUserService.updateReviewUser(id, reviewUser);
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/{id}")
    public void deleteReviewUser(@PathVariable int id) {
        reviewUserService.deleteReviewUser(id);
    }
}
