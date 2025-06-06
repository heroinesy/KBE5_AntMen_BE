package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.alert.AlertRequestDto;
import com.antmen.antwork.common.infra.repository.AlertRepository;
import com.antmen.antwork.common.service.mapper.AlertMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    public void sendAlert(AlertRequestDto alertRequestDto) {
        alertRepository.save(alertMapper.toEntity(alertRequestDto));
    }
}
