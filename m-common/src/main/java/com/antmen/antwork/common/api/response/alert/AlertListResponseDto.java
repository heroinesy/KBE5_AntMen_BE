package com.antmen.antwork.common.api.response.alert;

import com.antmen.antwork.common.domain.entity.Alert;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AlertListResponseDto {
    private Long alertId;
    private String alertContent;
    private String redirectUrl;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static AlertListResponseDto toListDto(Alert alert) {
        return AlertListResponseDto.builder()
                .alertId(alert.getAlertId())
                .alertContent(alert.getAlertContent())
                .redirectUrl(alert.getRedirectUrl())
                .isRead(alert.getIsRead())
                .createdAt(alert.getCreatedAt())
                .build();
    }
}
