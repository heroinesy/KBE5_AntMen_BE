package com.antmen.antwork.common.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import com.antmen.antwork.common.domain.entity.ReviewAuthorType;

@Getter
@Setter
@Builder
public class ReviewRequestDto {
    @NotNull
    private Long reservationId;
    @NotNull
    private Short reviewRating;
    private String reviewComment;
    @NotNull
    private ReviewAuthorType reviewAuthor;
} 