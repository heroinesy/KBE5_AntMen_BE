package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.AlertRequestDto;
import com.antmen.antwork.common.infra.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;

    public void sendAlert(AlertRequestDto alertRequestDto) {
        alertRepository.save(alertRequestDto);
    }
}
