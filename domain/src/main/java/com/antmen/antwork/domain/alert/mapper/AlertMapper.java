package com.antmen.antwork.domain.alert.mapper;

import com.antmen.antwork.domain.alert.dto.AlertRequestDto;
import com.antmen.antwork.domain.alert.entity.Alert;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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