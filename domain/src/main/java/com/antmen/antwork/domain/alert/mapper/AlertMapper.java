package com.antmen.antwork.domain.alert.mapper;







@Component
public class AlertMapper {
    public Alert toEntity(AlertRequestDto alertRequestDto) {
        return Alert.builder()
                .alertContent(alertRequestDto.getAlertContent())
                .alertTrigger(alertRequestDto.getAlertTrigger())
                .alertUserId(alertRequestDto.getUserId())
                .redirectUrl(alertRequestDto.getRedirectUrl())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
