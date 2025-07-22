package com.antmen.antwork.domain.matching.dto;






@Getter
@Setter
@ToString
@Builder
public class MatchingCancelRequestDto {
    private Boolean IsContinue;
    private String cancelReason;
}
