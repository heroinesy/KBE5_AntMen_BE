package com.antmen.antwork.customer.service;

import com.antmen.antwork.customer.dto.ReviewUserDTO;
import com.antmen.antwork.customer.repository.ReviewUserRepository;
import com.antmen.antwork.entity.ReviewUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewUserService {

    @Autowired
    private ReviewUserRepository reviewUserRepository;

    // 리뷰 생성 (CREATE)
    public ReviewUserDTO createReviewUser(ReviewUser reviewUser) {
        ReviewUser savedReview = reviewUserRepository.save(reviewUser);
        return new ReviewUserDTO(savedReview);
    }

    // 전체 리뷰 조회 (READ)
    public List<ReviewUserDTO> getAllReviewUsers() {
        return reviewUserRepository.findAll().stream()
                .map(ReviewUserDTO::new)
                .collect(Collectors.toList());
    }

    // 특정 리뷰 조회
    public ReviewUserDTO getReviewUserById(Integer id) {
        Optional<ReviewUser> reviewUser = reviewUserRepository.findById(id);
        return reviewUser.map(ReviewUserDTO::new).orElse(null);
    }

    // 리뷰 수정 (UPDATE)
    public ReviewUserDTO updateReviewUser(Integer id, ReviewUser updatedReview) {
        Optional<ReviewUser> reviewUser = reviewUserRepository.findById(id);

        if (reviewUser.isPresent()) {
            ReviewUser existingReview = reviewUser.get();
            existingReview.setRating(updatedReview.getRating());
            existingReview.setComment(updatedReview.getComment());
            ReviewUser savedReview = reviewUserRepository.save(existingReview);
            return new ReviewUserDTO(savedReview);
        }
        return null;
    }

    //리뷰 삭제 (DELETE)
    public void deleteReviewUser(Integer id) {
        reviewUserRepository.deleteById(id);
    }
}
