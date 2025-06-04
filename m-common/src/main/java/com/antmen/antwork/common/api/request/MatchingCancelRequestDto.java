package com.antmen.antwork.common.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class MatchingCancelRequestDto {
    private Boolean IsContinue;
    private String cancelReason;
}
