package com.antmen.antwork.common.api.request;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import com.antmen.antwork.common.domain.entity.ReviewAuthorType;

@Getter
@Setter
@Builder
public class ReviewRequestDto {
    private User reviewCustomer;
    private User reviewManager;
    private Reservation reservation;
    private Short reviewRating;
    private String reviewComment;
    private ReviewAuthorType reviewAuthor;
} 