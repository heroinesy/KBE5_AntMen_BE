package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.api.request.reservation.MatchingRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class MatchingConsumerService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final MatchingService matchingService;
    @Scheduled(fixedDelay = 2000) // 2초마다 큐 polling
    public void consume() {
        Object raw = redisTemplate.opsForList().leftPop("matching:queue");
        if (raw == null) return;

        try {
            MatchingRequestDto dto = objectMapper.convertValue(raw, MatchingRequestDto.class);
            log.info("📥 Redis 큐에서 매칭 요청 수신: {}", dto.getReservationId());

            matchingService.createInitialMatchingFromDto(dto);
        } catch (Exception e) {
            log.error("❌ 매칭 처리 실패: {}", e.getMessage(), e);
        }
    }
}