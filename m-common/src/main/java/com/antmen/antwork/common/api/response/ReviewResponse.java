package com.antmen.antwork.common.api.response;

import com.antmen.antwork.common.domain.entity.ReviewStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponse {

    private Long reviewId;

    private Long customerId;

    private Long managerId;

    private Long reservationId;

    private Short reviewRating;

    private String reviewComment;

    private ReviewStatus reviewStatus;

}
