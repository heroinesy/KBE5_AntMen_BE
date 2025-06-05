package com.antmen.antwork.common.service.mapper.reservation;

import com.antmen.antwork.common.api.request.reservation.ReviewRequestDto;
import com.antmen.antwork.common.api.response.reservation.ReviewResponseDto;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.Review;
import com.antmen.antwork.common.domain.entity.account.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public Review toEntity(ReviewRequestDto dto, User customer, User manager, Reservation reservation) {
        return Review.builder()
                .reviewCustomer(customer)
                .reviewManager(manager)
                .reservation(reservation)
                .reviewRating(dto.getReviewRating())
                .reviewComment(dto.getReviewComment())
                .reviewAuthor(dto.getReviewAuthor())
                .build();
    }

    public ReviewResponseDto toDto(Review review) {
        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .reviewCustomerId(review.getReviewCustomer().getUserId())
                .reviewCustomerName(review.getReviewCustomer().getUserName())
                .reviewCustomerProfile(review.getReviewCustomer().getUserProfile())
                .reviewManagerId(review.getReviewManager().getUserId())
                .reviewManagerName(review.getReviewManager().getUserName())
                .reviewManagerProfile(review.getReviewManager().getUserProfile())
                .reservationId(review.getReservation().getReservationId())
                .reviewRating(review.getReviewRating())
                .reviewComment(review.getReviewComment())
                .reviewAuthor(review.getReviewAuthor())
                .reviewDate(review.getReviewDate())
                .build();
    }
} 
