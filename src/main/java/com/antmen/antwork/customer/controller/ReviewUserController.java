package com.antmen.antwork.customer.controller;
import com.antmen.antwork.customer.dto.ReviewUserDTO;
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

    // 리뷰 생성
    @PostMapping
    public ReviewUserDTO createReviewUser(@RequestBody ReviewUser reviewUser) {
        return reviewUserService.createReviewUser(reviewUser);
    }

    // 전체 리뷰 조회
    @GetMapping
    public List<ReviewUserDTO> getAllReviewUsers() {
        return reviewUserService.getAllReviewUsers();
    }

    // 특정 리뷰 조회
    @GetMapping("/{id}")
    public ReviewUserDTO getReviewUserById(@PathVariable Integer id) {
        return reviewUserService.getReviewUserById(id);
    }

    // 리뷰 수정
    @PutMapping("/{id}")
    public ReviewUserDTO updateReviewUser(@PathVariable Integer id, @RequestBody ReviewUser reviewUser) {
        return reviewUserService.updateReviewUser(id, reviewUser);
    }

    //리뷰 삭제
    @DeleteMapping("/{id}")
    public void deleteReviewUser(@PathVariable Integer id) {
        reviewUserService.deleteReviewUser(id);
    }
}
