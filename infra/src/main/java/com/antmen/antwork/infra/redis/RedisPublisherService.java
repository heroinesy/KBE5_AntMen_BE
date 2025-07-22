package com.antmen.antwork.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisherService {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 지정된 채널로 메시지를 발행합니다.
     * @param channelName 채널 이름 (예: "user:123")
     * @param message 발행할 메시지 객체 (DTO 등)
     */
    public void publish(String channelName, Object message) {
        redisTemplate.convertAndSend(channelName, message);
    }
}
