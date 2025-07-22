package com.antmen.antwork.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerReviewDto {
    private Long reviewId;
    private Short rating;
    private String comment;
    private String reviewDate;
    private String targetName;  // 매니저 이름 또는 고객 이름
    private String targetProfile;
} 