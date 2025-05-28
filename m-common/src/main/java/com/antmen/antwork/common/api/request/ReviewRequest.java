package com.antmen.antwork.common.api.request;

import com.antmen.antwork.common.domain.entity.ReviewStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewRequest {

    @NotNull
    private Long reservationId;

    @NotNull
    private Short reviewRating;

    private String reviewComment;

}
