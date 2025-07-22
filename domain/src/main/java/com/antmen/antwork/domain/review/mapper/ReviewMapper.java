package com.antmen.antwork.domain.review.mapper;








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
