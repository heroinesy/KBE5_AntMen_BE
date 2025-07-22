package com.antmen.antwork.infra.redis;











@Configuration
public class RedisConfig {
    // 동적으로 리스너를 추가/삭제할 컨테이너만 Bean으로 등록합니다.
    @Bean
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // Pub/Sub 전용 쓰레드풀 설정 (기본값보다 명시적 지정)
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(4);
        scheduler.setThreadNamePrefix("redis-listener-");
        scheduler.initialize();
        container.setTaskExecutor(scheduler);
        container.setRecoveryInterval(3000L); // Redis 끊겼을 때 재시도 주기

        return container;
    }

    // 메시지 발행에 사용할 RedisTemplate을 설정합니다.
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key-Value, Hash-Key-Value 직렬화 방식을 설정합니다.
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, Object.class)); // ObjectMapper를 사용하도록 수정
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, Object.class)); // ObjectMapper를 사용하도록 수정

        return template;
    }
}
