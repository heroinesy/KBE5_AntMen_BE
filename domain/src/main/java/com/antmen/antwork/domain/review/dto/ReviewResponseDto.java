package com.antmen.antwork.domain.review.dto;

import com.antmen.antwork.domain.review.entity.ReviewAuthorType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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