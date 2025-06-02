package com.antmen.antwork.common.infra.repository;

import com.antmen.antwork.common.domain.entity.Review;
import com.antmen.antwork.common.domain.entity.ReviewAuthorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewAuthorAndReviewCustomer_UserId(ReviewAuthorType reviewAuthor, Long reviewCustomerUserId);

    List<Review> findByReviewAuthorAndReviewManager_UserId(ReviewAuthorType reviewAuthorType, Long loginId);
}
