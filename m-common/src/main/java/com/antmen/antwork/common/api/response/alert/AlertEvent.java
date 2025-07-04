package com.antmen.antwork.common.api.response.alert;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class AlertEvent {
    private final Long userId;
    private final String alertContent;
    private final String alertTrigger;
    private final String redirectUrl;
}
