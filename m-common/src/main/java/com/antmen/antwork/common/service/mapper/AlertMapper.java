package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.request.alert.AlertRequestDto;
import com.antmen.antwork.common.domain.entity.Alert;
import org.springframework.stereotype.Component;

@Component
public class AlertMapper {
    public Alert toEntity(AlertRequestDto alertRequestDto) {
        return Alert.builder()
                .alertContent(alertRequestDto.getAlertContent())
                .alertTrigger(alertRequestDto.getAlertTrigger())
                .alertUserId(alertRequestDto.getUserId())
                .build();
    }
}
