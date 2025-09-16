package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.api.request.reservation.MatchingRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class MatchingConsumerService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final MatchingService matchingService;

    @Scheduled(fixedDelay = 2000)
    public void consume() {
        Object raw = redisTemplate.opsForList().leftPop("matching:queue");
        if (raw == null) return;

        try {
            String json = (String) raw;
            MatchingRequestDto dto = objectMapper.readValue(json, MatchingRequestDto.class);

            LocalDateTime startTime = LocalDateTime.now();
            long start = System.currentTimeMillis();

            log.info("📥 [{}] Redis 큐에서 매칭 요청 수신: reservationId={}", startTime, dto.getReservationId());

            matchingService.createInitialMatchingFromDto(dto);

            long end = System.currentTimeMillis();
            long duration = end - start;
            log.info("✅ [{}] 매칭 처리 완료: reservationId={}, 처리시간={}ms", LocalDateTime.now(), dto.getReservationId(), duration);

        } catch (Exception e) {
            log.error("❌ [{}] 매칭 처리 실패: {}", LocalDateTime.now(), e.getMessage(), e);
        }
    }
}