package com.antmen.antwork.common.util.log;

/*
=====================================================
🧹 PerformanceMonitoringAspect 주석처리됨
=====================================================

복잡한 성능 모니터링 AOP를 주석처리했습니다.
개발 중에는 간단한 로그만 사용하고,
필요시 주석을 해제하여 성능 모니터링을 활성화할 수 있습니다.

=====================================================
*/

import lombok.extern.slf4j.Slf4j;
// import org.aspectj.lang.ProceedingJoinPoint;
// import org.aspectj.lang.annotation.Around;
// import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 성능 모니터링 AOP (주석처리됨)
 * - 메서드 실행 시간 측정
 * - CPU 사용률 모니터링
 * - 느린 메서드 Thread Dump 생성
 */
// @Aspect  // 🧹 주석처리: AOP 비활성화
@Component
@Slf4j
public class PerformanceMonitoringAspect {
    
    // 🧹 모든 성능 모니터링 기능 주석처리
    /*
    private static final Logger PERFORMANCE_LOGGER = LoggerFactory.getLogger("PERFORMANCE_LOG");
    private final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    
    @Autowired
    private BusinessLogger businessLogger;
    
    // 성능 임계치 설정
    private static final long SLOW_METHOD_THRESHOLD = 3000; // 3초 (경고)
    private static final long VERY_SLOW_METHOD_THRESHOLD = 15000; // 15초 (Thread Dump)
    private static final long CRITICAL_SLOW_THRESHOLD = 30000; // 30초 (심각한 문제)
    private static final double HIGH_CPU_THRESHOLD = 80.0; // 80%
    
    // Thread Dump 생성 간격 제한 (1분에 한 번)
    private long lastThreadDumpTime = 0;
    private static final long THREAD_DUMP_INTERVAL = 60000; // 60초
    
    @Around("execution(* com.antmen.antwork.*.controller..*(..))")
    public Object monitorController(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorMethod(joinPoint, "CONTROLLER");
    }
    
    @Around("execution(* com.antmen.antwork.*.service..*(..))")
    public Object monitorService(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorMethod(joinPoint, "SERVICE");
    }
    
    @Around("execution(* com.antmen.antwork.*.repository..*(..))")
    public Object monitorRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorMethod(joinPoint, "REPOSITORY");
    }
    
    private Object monitorMethod(ProceedingJoinPoint joinPoint, String type) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String fullMethodName = className + "." + methodName;
        
        // 성능 측정 시작
        long startTime = System.currentTimeMillis();
        long startCpuTime = threadBean.getCurrentThreadCpuTime();
        
        // MDC에 성능 모니터링 컨텍스트 설정
        MDC.put("performanceType", type);
        MDC.put("targetName", fullMethodName);
        
        try {
            // 실제 메서드 실행
            Object result = joinPoint.proceed();
            
            // 성능 측정 완료
            long duration = System.currentTimeMillis() - startTime;
            long cpuTime = (threadBean.getCurrentThreadCpuTime() - startCpuTime) / 1_000_000; // ns -> ms
            
            // MDC에 결과 설정
            MDC.put("duration", String.valueOf(duration));
            
            // 성능 로그 기록
            logPerformance(type, fullMethodName, duration, cpuTime);
            
            // 임계치 검사 및 추가 처리
            checkPerformanceThresholds(type, fullMethodName, duration, cpuTime);
            
            return result;
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            MDC.put("duration", String.valueOf(duration));
            
            PERFORMANCE_LOGGER.error("❌ {} 실행 중 에러 발생: {} ({}ms)", type, fullMethodName, duration, e);
            throw e;
            
        } finally {
            // MDC 정리
            MDC.remove("performanceType");
            MDC.remove("targetName");
            MDC.remove("duration");
        }
    }
    
    private void logPerformance(String type, String methodName, long duration, long cpuTime) {
        if (duration > SLOW_METHOD_THRESHOLD) {
            PERFORMANCE_LOGGER.warn("🐌 느린 {} 메서드: {} - 실행시간: {}ms, CPU시간: {}ms", 
                type, methodName, duration, cpuTime);
        } else {
            PERFORMANCE_LOGGER.debug("⚡ {} 메서드 실행: {} - 실행시간: {}ms, CPU시간: {}ms", 
                type, methodName, duration, cpuTime);
        }
    }
    
    private void checkPerformanceThresholds(String type, String methodName, long duration, long cpuTime) {
        // 매우 느린 메서드 - Thread Dump 생성
        if (duration > VERY_SLOW_METHOD_THRESHOLD) {
            generateThreadDumpIfNeeded(type, methodName, duration);
        }
        
        // 심각하게 느린 메서드
        if (duration > CRITICAL_SLOW_THRESHOLD) {
            PERFORMANCE_LOGGER.error("🚨 CRITICAL: {} 메서드가 {}ms 소요 - 즉시 확인 필요: {}", 
                type, duration, methodName);
            
            // 비즈니스 로거에도 기록
            if (businessLogger != null) {
                businessLogger.logSlowBusiness("CRITICAL_SLOW", null, null, duration, methodName);
            }
        }
        
        // CPU 사용률 높음
        double cpuUsagePercent = (cpuTime * 100.0) / duration;
        if (cpuUsagePercent > HIGH_CPU_THRESHOLD) {
            PERFORMANCE_LOGGER.warn("🔥 높은 CPU 사용률: {} - CPU: {:.1f}% ({}ms 중 {}ms)", 
                methodName, cpuUsagePercent, duration, cpuTime);
        }
    }
    
    private void generateThreadDumpIfNeeded(String type, String methodName, long duration) {
        long currentTime = System.currentTimeMillis();
        
        // 1분 간격으로만 Thread Dump 생성 (너무 자주 생성 방지)
        if (currentTime - lastThreadDumpTime < THREAD_DUMP_INTERVAL) {
            return;
        }
        
        lastThreadDumpTime = currentTime;
        
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = String.format("/tmp/thread-dumps/slow-method_%s_%s.txt", 
                timestamp, methodName.replace(".", "_"));
            
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(String.format("=== Thread Dump for Slow Method ===\n"));
                writer.write(String.format("Timestamp: %s\n", timestamp));
                writer.write(String.format("Method: %s (%s)\n", methodName, type));
                writer.write(String.format("Duration: %dms\n", duration));
                writer.write(String.format("Threshold: %dms\n\n", VERY_SLOW_METHOD_THRESHOLD));
                
                // 모든 스레드 정보 덤프
                ThreadMXBean threadMX = ManagementFactory.getThreadMXBean();
                long[] threadIds = threadMX.getAllThreadIds();
                
                for (long threadId : threadIds) {
                    ThreadInfo threadInfo = threadMX.getThreadInfo(threadId, Integer.MAX_VALUE);
                    if (threadInfo != null) {
                        writer.write(String.format("Thread: %s (ID: %d, State: %s)\n", 
                            threadInfo.getThreadName(), threadId, threadInfo.getThreadState()));
                        
                        StackTraceElement[] stackTrace = threadInfo.getStackTrace();
                        for (StackTraceElement element : stackTrace) {
                            writer.write(String.format("  at %s\n", element));
                        }
                        writer.write("\n");
                    }
                }
            }
            
            PERFORMANCE_LOGGER.warn("📄 Thread Dump 생성 완료: {} ({}ms 소요 메서드: {})", 
                fileName, duration, methodName);
            
        } catch (IOException e) {
            PERFORMANCE_LOGGER.error("Thread Dump 생성 실패: {}", e.getMessage(), e);
        }
    }
    
    @EventListener(ContextClosedEvent.class)
    public void onApplicationShutdown() {
        PERFORMANCE_LOGGER.info("🏁 성능 모니터링 종료 - 애플리케이션 종료");
    }
    
    @Scheduled(fixedRate = 300000) // 5분마다
    public void logSystemPerformance() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        double memoryUsagePercent = (usedMemory * 100.0) / totalMemory;
        
        PERFORMANCE_LOGGER.info("💻 시스템 성능: 메모리 사용률: {:.1f}% ({}/{}MB)", 
            memoryUsagePercent, 
            usedMemory / (1024 * 1024), 
            totalMemory / (1024 * 1024));
        
        if (memoryUsagePercent > 85.0) {
            PERFORMANCE_LOGGER.warn("⚠️ 높은 메모리 사용률: {:.1f}%", memoryUsagePercent);
        }
    }
    */
    
    // 🧹 간단한 로그만 남김 (개발 편의성)
    public void logSimplePerformance(String operation, long durationMs) {
        if (durationMs > 1000) { // 1초 이상만 로그
            log.warn("느린 작업: {} - {}ms", operation, durationMs);
        }
    }
} 