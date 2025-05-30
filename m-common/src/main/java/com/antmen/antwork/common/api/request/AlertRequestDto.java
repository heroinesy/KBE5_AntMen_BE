package com.antmen.antwork.common.api.request;

import com.antmen.antwork.common.domain.entity.Alert;
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

}
