package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.request.ReviewRequestDto;
import com.antmen.antwork.common.api.response.ReviewResponseDto;
import com.antmen.antwork.common.domain.entity.Review;
import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.domain.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public Review toEntity(ReviewRequestDto dto, User customer, User manager, Reservation reservation) {
        if (dto == null) return null;
        return Review.builder()
                .reviewCustomer(customer)
                .reviewManager(manager)
                .reservation(reservation)
                .reviewRating(dto.getReviewRating())
                .reviewComment(dto.getReviewComment())
                .reviewAuThor(dto.getReviewAuthor())
                .build();
    }

    public ReviewResponseDto toDto(Review review) {
        if (review == null) return null;
        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .reviewCustomer(review.getReviewCustomer())
                .reviewManager(review.getReviewManager())
                .reservation(review.getReservation())
                .reviewRating(review.getReviewRating())
                .reviewComment(review.getReviewComment())
                .reviewAuthor(review.getReviewAuThor())
                .build();
    }
} 