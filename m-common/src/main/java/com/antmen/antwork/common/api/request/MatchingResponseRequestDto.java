package com.antmen.antwork.common.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class MatchingResponseRequestDto {
    private Boolean matchingIsFinal;
    private String matchingRefuseReason;
}
