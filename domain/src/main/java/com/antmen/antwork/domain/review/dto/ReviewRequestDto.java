package com.antmen.antwork.domain.review.dto;







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