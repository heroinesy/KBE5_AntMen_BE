package com.antmen.antwork.common.api.response.reservation;

import com.antmen.antwork.common.domain.entity.ReviewSummary;
import com.antmen.antwork.common.domain.entity.reservation.Matching;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MatchingDto {
    private Long matchingId;
    private int priority;
    private Boolean isRequested;
    private Boolean isAccepted;
    private Boolean isFinal;
    private String refuseReason;
    private UserSummaryDto manager;
    private LocalDateTime updatedAt;

    public static MatchingDto from(Matching matching, ReviewSummary managerSummary) {
        return MatchingDto.builder()
                .matchingId(matching.getMatchingId())
                .priority(matching.getMatchingPriority())
                .isRequested(matching.getMatchingIsRequest())
                .isAccepted(matching.getMatchingManagerIsAccept())
                .isFinal(matching.getMatchingIsFinal())
                .refuseReason(matching.getMatchingRefuseReason())
                .manager(UserSummaryDto.from(matching.getManager(), managerSummary))
                .updatedAt(matching.getMatchingUpdatedAt())
                .build();
    }
}