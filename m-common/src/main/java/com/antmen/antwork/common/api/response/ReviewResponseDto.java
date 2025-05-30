package com.antmen.antwork.common.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import com.antmen.antwork.common.domain.entity.ReviewAuthorType;
import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.domain.entity.Reservation;

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