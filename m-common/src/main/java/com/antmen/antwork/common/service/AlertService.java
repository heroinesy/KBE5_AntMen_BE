package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.alert.AlertRequestDto;
import com.antmen.antwork.common.api.response.alert.AlertListResponseDto;
import com.antmen.antwork.common.domain.entity.Alert;
import com.antmen.antwork.common.domain.entity.AlertTrigger;
import com.antmen.antwork.common.infra.repository.AlertRepository;
import com.antmen.antwork.common.service.mapper.AlertMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final ObjectMapper objectMapper;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간

    private final RedisPublisherService redisPublisherService;
    private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();
    private final Map<Long, MessageListener> listenerMap = new ConcurrentHashMap<>();
    private final Map<Long, Object> userLocks = new ConcurrentHashMap<>();

    // SSE 구독
    public SseEmitter subscribe(Long userId) {
        // 사용자별 락
        Object lock = userLocks.computeIfAbsent(userId, k -> new Object());
        synchronized (lock) {

            SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
            String eventId = userId + "_" + System.currentTimeMillis();

            // 기존 Emitter가 있는지 확인
            SseEmitter oldEmitter = emitterMap.put(userId, emitter);
            if (oldEmitter != null) {
                oldEmitter.complete();
            }

            try {
                emitter.send(SseEmitter.event().id(eventId).name("connect").data("Connected!"));

            } catch (IOException e) {
                log.error("SSE 연결 오류: userId={}, 에러={}", userId, e.getMessage());
                emitter.completeWithError(e);
                emitterMap.remove(userId);
                return emitter;
            }

            String channelName = "user:" + userId;
            MessageListener listener = (message, pattern) -> {
                try {
                    AlertRequestDto alertRequestDto = objectMapper.readValue(message.getBody(), AlertRequestDto.class);

                    AlertListResponseDto responseDto = AlertListResponseDto.builder()
                            .alertId(null)
                            .alertContent(alertRequestDto.getAlertContent())
                            .redirectUrl(alertRequestDto.getRedirectUrl())
                            .isRead(false)
                            .createdAt(LocalDateTime.now())
                            .build();

                    String newEventId = userId + "_" + System.currentTimeMillis();
                    emitter.send(SseEmitter.event()
                            .id(newEventId)
                            .name("alert")
                            .data(responseDto));

                } catch (IOException e) {
                    log.warn("❌ SSE 알림 전송 실패 → 연결 종료: userId={}, 에러={}", userId, e.getMessage());
                    emitter.completeWithError(e);
                    emitterMap.remove(userId);
                } catch (Exception ex) {
                    log.warn("❌ Redis 메시지 처리 실패: userId={}, 에러={}", userId, ex.getMessage(), ex);
                }
            };

            // 중복 Redis 리스너 제거
            MessageListener oldListener = listenerMap.put(userId, listener);
            if (oldListener != null) {
                redisMessageListenerContainer.removeMessageListener(oldListener);
            }

            redisMessageListenerContainer.addMessageListener(listener, new ChannelTopic(channelName));

            Runnable cleanup = () -> {
                redisMessageListenerContainer.removeMessageListener(listener);
                emitterMap.remove(userId);
                listenerMap.remove(userId);
                userLocks.remove(userId);
            };

            emitter.onCompletion(cleanup);
            emitter.onTimeout(cleanup);
            emitter.onError(e -> cleanup.run());

            return emitter;
        }
    }

    @Transactional
    public void sendAlert(Long userId, AlertTrigger alertTrigger, Long reservationId) {
        String channel = "user:" + userId;
        String redirectUrl = generateRedirectUrl(alertTrigger, reservationId);

        AlertRequestDto alertDto = AlertRequestDto.builder()
                .userId(userId)
                .alertContent(alertTrigger.getContent())
                .alertTrigger(String.valueOf(alertTrigger))
                .redirectUrl(redirectUrl)
                .build();

        // Redis로 실시간 알림 발송
        redisPublisherService.publish(channel, alertDto);

        // DB에 알림 데이터 저장
        saveAlert(alertDto);
    }

    private String generateRedirectUrl(AlertTrigger trigger, Long reservationId) {

        return switch (trigger) {
            case MATCHING_REQUEST_TO_MANAGER, MATCHING_LOST_TO_MANAGER, RESERVATION_CANCELED ->
                    "/manager/matching/" + reservationId;

            case MATCHING_ACCEPTED_BY_MANAGER,RESERVATION_CONFIRMED, SERVICE_CHECK_IN, SERVICE_CHECK_OUT ->
                    "/myreservation/" + reservationId;

            case MATCHING_CONFIRMED_BY_CUSTOMER ->
                    "/manager/reservations/" + reservationId;

        };
    }

    @Transactional
    public void saveAlert(AlertRequestDto alertRequestDto) {
        Alert alert = alertMapper.toEntity(alertRequestDto);
        alertRepository.save(alert);
    }

    @Transactional(readOnly = true)
    public List<AlertListResponseDto> getAlertList(Long userId) {
        return alertRepository.findAllByAlertUserIdOrderByCreatedAtDesc(userId).stream()
                .map(AlertListResponseDto::toListDto).toList();
    }

    @Transactional
    public void readAllAlert(Long userId) {
        List<Alert> unReadAlertList = alertRepository.findAllByAlertUserIdAndIsReadFalse(userId);

        for (Alert alert : unReadAlertList) {
            alert.setIsRead(true);
        }
    }

    @Transactional
    public void readAlert(Long userId, Long alertId) {
        Alert alert = alertRepository.findByAlertIdAndAlertUserId(alertId, userId)
                .orElseThrow(()->new RuntimeException("해당 알림을 읽을 권한이 없습니다."));

        alert.setIsRead(true);

    }

    @Transactional(readOnly = true)
    public AlertListResponseDto getAlert(Long userId, Long alertId) {
        Alert alert = alertRepository.findByAlertIdAndAlertUserId(alertId, userId)
                .orElseThrow(() -> new RuntimeException("해당 알림을 읽을 권한이 없습니다."));

        return AlertListResponseDto.toListDto(alert);

    }
}
