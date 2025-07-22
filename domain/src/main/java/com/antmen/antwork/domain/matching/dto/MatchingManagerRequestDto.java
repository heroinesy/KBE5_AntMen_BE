package com.antmen.antwork.domain.matching.dto;






@Getter
@Setter
@ToString
@Builder
public class MatchingManagerRequestDto {
    Boolean matchingManagerIsAccept;
    private String matchingRefuseReason;
}
