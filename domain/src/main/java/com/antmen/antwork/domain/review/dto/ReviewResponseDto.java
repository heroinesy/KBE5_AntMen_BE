package com.antmen.antwork.domain.review.dto;








@Getter
@Setter
@Builder
public class ReviewResponseDto {
    private Long reviewId;
    private Long reviewCustomerId;
    private String reviewCustomerName;
    private String reviewCustomerProfile;
    private Long reviewManagerId;
    private String reviewManagerName;
    private String reviewManagerProfile;
    private Long reservationId;
    private Short reviewRating;
    private String reviewComment;
    private ReviewAuthorType reviewAuthor;
    private LocalDateTime reviewDate;
} 