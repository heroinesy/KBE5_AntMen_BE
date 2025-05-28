package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.request.ReviewRequest;
import com.antmen.antwork.common.api.response.ReviewResponse;
import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.domain.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(Reservation reservation, ReviewRequest reviewRequest){
        return Review.builder()
                .reviewCustomer(reservation.getCustomer())
                .reviewManager(reservation.getManager())
                .reservation(reservation)
                .reviewComment(reviewRequest.getReviewComment())
                .reviewRating(reviewRequest.getReviewRating())
                .build();
    }

    public ReviewResponse toDto(Review review){
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .customerId(review.getReviewCustomer().getUserId())
                .managerId(review.getReviewManager().getUserId())
                .reservationId(review.getReservation().getReservationId())
                .reviewRating(review.getReviewRating())
                .reviewComment(review.getReviewComment())
                .reviewStatus(review.getReviewStatus())
                .build();
    }
}
