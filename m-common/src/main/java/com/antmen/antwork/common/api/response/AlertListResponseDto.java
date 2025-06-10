package com.antmen.antwork.common.api.response;

import com.antmen.antwork.common.domain.entity.Alert;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AlertListResponseDto {
    private Long alertId;
    private String alertContent;

    public static AlertListResponseDto toDto(Alert alert) {
        return AlertListResponseDto.builder()
                .alertId(alert.getAlertId())
                .alertContent(alert.getAlertContent())
                .build();
    }
}
