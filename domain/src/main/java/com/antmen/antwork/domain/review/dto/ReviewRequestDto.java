package com.antmen.antwork.domain.review.dto;

import com.antmen.antwork.domain.review.entity.ReviewAuthorType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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