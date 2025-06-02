package com.antmen.antwork.manager.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class MatchingManagerRequestDto {
    Boolean matchingManagerIsAccept;
    private String matchingRefuseReason;
}
