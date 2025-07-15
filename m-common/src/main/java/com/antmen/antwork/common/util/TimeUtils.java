package com.antmen.antwork.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * UTC 시간 생성을 위한 유틸리티 클래스
 * 
 * 백엔드에서 일관된 UTC 시간을 사용하기 위해 제공됩니다.
 * 모든 시간 관련 생성은 이 클래스를 통해 수행해야 합니다.
 */
public class TimeUtils {
    
    /**
     * 현재 UTC 시간을 LocalDateTime으로 반환
     * 
     * @return 현재 UTC 시간
     */
    public static LocalDateTime nowUTC() {
        return LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
    }
    
    /**
     * 현재 UTC 날짜를 LocalDate로 반환
     * 
     * @return 현재 UTC 날짜
     */
    public static LocalDate todayUTC() {
        return LocalDate.ofInstant(Instant.now(), ZoneOffset.UTC);
    }
    
    /**
     * 특정 Instant를 UTC LocalDateTime으로 변환
     * 
     * @param instant 변환할 Instant
     * @return UTC LocalDateTime
     */
    public static LocalDateTime fromInstant(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
    
    /**
     * 현재 시간을 Instant로 반환
     * 
     * @return 현재 Instant
     */
    public static Instant nowInstant() {
        return Instant.now();
    }
} 