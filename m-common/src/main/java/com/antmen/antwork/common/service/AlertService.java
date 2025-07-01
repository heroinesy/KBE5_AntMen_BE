package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.alert.AlertRequestDto;
import com.antmen.antwork.common.api.response.alert.AlertListResponseDto;
import com.antmen.antwork.common.domain.entity.Alert;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.AlertRepository;
import com.antmen.antwork.common.service.mapper.AlertMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    // SSE 연결을 관리하기 위한 Map
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    // SSE 구독
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);

        // 연결 종료/에러시 emitter 제거
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        // 최초 연결시 더미 이벤트 전송
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Connected!"));
        } catch (IOException e) {
            emitters.remove(userId);
        }

        return emitter;
    }

    public void sendAlert(AlertRequestDto alertRequestDto) {
        Alert alert = alertMapper.toEntity(alertRequestDto);
        alertRepository.save(alert);

        // SSE로 실시간 알림 전송
        SseEmitter emitter = emitters.get(alertRequestDto.getUserId());
        if (emitter != null) {
            try {
                AlertListResponseDto alertDto = AlertListResponseDto.toListDto(alert);
                emitter.send(SseEmitter.event()
                        .name("alert")
                        .data(alertDto));
            } catch (IOException e) {
                log.warn("SSE 데이터 전송 중 IO 예외 발생 (클라이언트 연결 끊김 가능성 높음): {}", e.getMessage());
                emitters.remove(alertRequestDto.getUserId());
            } catch (Exception e) {
                log.error("SSE 데이터 전송 중 알 수 없는 예외 발생", e);
                emitters.remove(alertRequestDto.getUserId());
            }
        }
    }

    public List<AlertListResponseDto> getAlertList(Long userId) {
        return alertRepository.findAllByAlertUserIdOrderByCreatedAtDesc(userId).stream()
                .map(AlertListResponseDto::toListDto).toList();
    }

    public void readAllAlert(Long userId) {
        List<Alert> unReadAlertList = alertRepository.findAllByAlertUserIdAndIsReadFalse(userId);

        for (Alert alert : unReadAlertList) {
            alert.setIsRead(true);
        }
    }

    public void readAlert(Long userId, Long alertId) {
        Alert alert = alertRepository.findByAlertIdAndAlertUserId(alertId, userId)
                .orElseThrow(()->new RuntimeException("해당 알림을 읽을 권한이 없습니다."));

        alert.setIsRead(true);
        alertRepository.save(alert);

    }

    public AlertListResponseDto getAlert(Long userId, Long alertId) {
        Alert alert = alertRepository.findByAlertIdAndAlertUserId(alertId, userId)
                .orElseThrow(() -> new RuntimeException("해당 알림을 읽을 권한이 없습니다."));

        return AlertListResponseDto.toListDto(alert);

    }
}
