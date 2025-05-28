package com.antmen.antwork.common.api.request;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import com.antmen.antwork.common.domain.entity.ReviewAuthorType;

@Getter
@Setter
@Builder
public class ReviewRequestDto {
    private Long reviewCustomerId;
    private Long reviewManagerId;
    private Long reservationId;
    private Short reviewRating;
    private String reviewComment;
    private ReviewAuthorType reviewAuthor;
} 