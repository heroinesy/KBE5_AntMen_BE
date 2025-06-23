package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.reservation.Review;
import com.antmen.antwork.common.domain.entity.reservation.ReviewAuthorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewAuthorAndReviewCustomer_UserId(ReviewAuthorType reviewAuthor, Long reviewCustomerUserId);

    List<Review> findByReviewAuthorAndReviewManager_UserId(ReviewAuthorType reviewAuthorType, Long loginId);

    boolean existsByReservation_ReservationIdAndReviewAuthorAndReviewCustomer_UserId(Long reservationId,ReviewAuthorType reviewAuthortype, Long loginId);
    boolean existsByReservation_ReservationIdAndReviewAuthorAndReviewManager_UserId(Long reservationId, ReviewAuthorType reviewAuthorType, Long loginId);


}
