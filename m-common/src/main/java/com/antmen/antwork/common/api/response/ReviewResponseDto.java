package com.antmen.antwork.common.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import com.antmen.antwork.common.domain.entity.ReviewAuthorType;
import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.domain.entity.Reservation;

@Getter
@Setter
@Builder
public class ReviewResponseDto {
    private Long reviewId;
    private User reviewCustomer;
    private User reviewManager;
    private Reservation reservation;
    private Short reviewRating;
    private String reviewComment;
    private ReviewAuthorType reviewAuthor;
} 