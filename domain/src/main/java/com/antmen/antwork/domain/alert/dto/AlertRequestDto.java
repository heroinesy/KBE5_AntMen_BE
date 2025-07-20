package com.antmen.antwork.domain.alert.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AlertRequestDto {
    private Long userId;
    private String alertContent;
    private String alertTrigger;
    private String redirectUrl;
}
