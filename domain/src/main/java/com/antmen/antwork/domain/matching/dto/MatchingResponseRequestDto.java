package com.antmen.antwork.domain.matching.dto;






@Getter
@Setter
@ToString
@Builder
public class MatchingResponseRequestDto {
    private Boolean matchingIsFinal;
    private String matchingRefuseReason;
}
