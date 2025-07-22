package com.antmen.antwork.domain.alert.service;

import com.antmen.antwork.domain.alert.dto.AlertListResponseDto;
import com.antmen.antwork.domain.alert.dto.AlertRequestDto;
import com.antmen.antwork.domain.alert.entity.Alert;
import com.antmen.antwork.domain.alert.entity.AlertTrigger;
import com.antmen.antwork.domain.alert.mapper.AlertMapper;
import com.antmen.antwork.domain.alert.repository.AlertRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
public class AlertService implements DisposableBean {
    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final ObjectMapper objectMapper;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간
    private final RedisPublisherService redisPublisherService;

    // 리소스 관리를 위한 필드
    private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();
    private final Map<Long, MessageListener> listenerMap = new ConcurrentHashMap<>();
    private final Map<Long, Object> userLocks = new ConcurrentHashMap<>();
    private final Map<Long, ScheduledFuture<?>> heartbeatTasks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    // SSE 구독
    public SseEmitter subscribe(Long userId) {
        // 사용자별 락
        Object lock = userLocks.computeIfAbsent(userId, k -> new Object());

        // 경쟁 상태 원천 차단
        synchronized (lock) {

            // 기존 연결이 있다면 관련 리소스 정리
            if (emitterMap.containsKey(userId)) {
                emitterMap.get(userId).complete();
            }

            // 새로운 Emitter를 생성 및 등록
            SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
            emitterMap.put(userId, emitter);

            // Heartbeat 생성 및 등록
            ScheduledFuture<?> heartbeatTask = scheduler.scheduleAtFixedRate(() -> {
                try {
                    emitter.send(SseEmitter.event().comment("keep-alive"));
                } catch (IOException e) {
                    log.warn("[SSE-{}] Heartbeat 전송 실패, 연결을 종료합니다.", userId);
                    emitter.complete();
                }
            }, 20, 20, TimeUnit.SECONDS);
            heartbeatTasks.put(userId, heartbeatTask);

            // Redis 리스너 생성 및 등록
            MessageListener listener = (message, pattern) -> {
                try {

                    AlertRequestDto alertRequestDto = objectMapper.readValue(message.getBody(), AlertRequestDto.class);

                    AlertListResponseDto responseDto = AlertListResponseDto.builder()
                            .alertId(null)
                            .alertContent(alertRequestDto.getAlertContent())
                            .redirectUrl(alertRequestDto.getRedirectUrl())
                            .alertTrigger(alertRequestDto.getAlertTrigger())
                            .isRead(false)
                            .createdAt(LocalDateTime.now())
                            .build();

                    emitter.send(SseEmitter.event()
                            .name("alert")
                            .data(responseDto));

                } catch (IOException e) {
                    emitter.complete();
                } catch (Exception ex) {
                    emitter.completeWithError(ex);
                }
            };
            redisMessageListenerContainer.addMessageListener(listener, new ChannelTopic("user:" + userId));
            listenerMap.put(userId, listener);

            // 연결 종료 시 최종 정리
            Runnable cleanup = () -> {
                ScheduledFuture<?> task = heartbeatTasks.remove(userId);
                if (task != null) {
                    task.cancel(true);
                }

                redisMessageListenerContainer.removeMessageListener(listener);
                listenerMap.remove(userId, listener);

                emitterMap.remove(userId, emitter);
            };

            emitter.onCompletion(cleanup);
            emitter.onTimeout(cleanup);
            emitter.onError(e -> cleanup.run());

            // 초기 연결 메시지
            try {
                emitter.send(SseEmitter.event().id(userId.toString()).name("connect").data("Connection established."));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }

            return emitter;
        }
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

    private String generateRedirectUrl(AlertTrigger trigger, Long id) {

        return switch (trigger) {
            case MATCHING_REQUEST_TO_MANAGER, MATCHING_LOST_TO_MANAGER, RESERVATION_CANCELED ->
                    "/manager/matching/" + id;

            case MATCHING_ACCEPTED_BY_MANAGER,RESERVATION_CONFIRMED, SERVICE_CHECK_IN, SERVICE_CHECK_OUT ->
                    "/myreservation/" + id;

            case MATCHING_CONFIRMED_BY_CUSTOMER ->
                    "/manager/reservations/" + id;

            case NOTICE_NEW_FOR_MANAGER, COMMENT_ON_POST_FOR_MANAGER ->
                    "/manager/boards/" + id;

            case NOTICE_NEW_FOR_CUSTOMER, COMMENT_ON_POST_FOR_CUSTOMER ->
                    "/boards/" + id;

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

    // 애플리케이션 종료 시 스케줄러를 안전하게 종료
    @Override
    public void destroy() throws Exception {
        log.info("애플리케이션 종료. SSE heartbeat 스케줄러를 종료합니다.");

        for (Map.Entry<Long, ScheduledFuture<?>> entry : heartbeatTasks.entrySet()) {
            entry.getValue().cancel(true);
            log.info("사용자 {}의 Heartbeat 취소 완료", entry.getKey());
        }

        scheduler.shutdown();

        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("scheduler 강제 종료");
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.warn("scheduler 종료 대기 중 인터럽트 발생", e);
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
