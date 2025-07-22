package com.antmen.antwork.domain.alert.dto;








@Getter
@Setter
@Builder
public class AlertListResponseDto {
    private Long alertId;
    private String alertContent;
    private String alertTrigger;
    private String redirectUrl;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static AlertListResponseDto toListDto(Alert alert) {
        return AlertListResponseDto.builder()
                .alertId(alert.getAlertId())
                .alertContent(alert.getAlertContent())
                .alertTrigger(alert.getAlertTrigger())
                .redirectUrl(alert.getRedirectUrl())
                .isRead(alert.getIsRead())
                .createdAt(alert.getCreatedAt())
                .build();
    }
}
