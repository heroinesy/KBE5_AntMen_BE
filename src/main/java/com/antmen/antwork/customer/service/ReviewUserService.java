package com.antmen.antwork.customer.service;

import com.antmen.antwork.customer.repository.ReviewUserRepository;
import com.antmen.antwork.entity.ReviewUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewUserService {

    @Autowired
    private ReviewUserRepository reviewUserRepository;

    /**
     * 리뷰 등록
     */
    public ReviewUser createReviewUser(ReviewUser reviewUser) {
        return reviewUserRepository.save(reviewUser);
    }

    /**
     * 전체 리뷰 조회
     */
    public List<ReviewUser> getAllReviewUsers() {
        return reviewUserRepository.findAll();
    }

    /**
     * 특정 리뷰 조회
     */
    public Optional<ReviewUser> getReviewUserById(int id) {
        return reviewUserRepository.findById(id);
    }

    /**
     * 리뷰 수정
     */
    public ReviewUser updateReviewUser(int id, ReviewUser updatedReview) {
        Optional<ReviewUser> reviewUser = reviewUserRepository.findById(id);
        if (reviewUser.isPresent()) {
            ReviewUser existingReview = reviewUser.get();
            existingReview.setRating(updatedReview.getRating());
            existingReview.setComment(updatedReview.getComment());
            return reviewUserRepository.save(existingReview);
        }
        return null;
    }

    /**
     * 리뷰 삭제
     */
    public void deleteReviewUser(int id) {
        reviewUserRepository.deleteById(id);
    }
}
