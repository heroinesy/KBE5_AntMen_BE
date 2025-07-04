package com.antmen.antwork.common.util.log;

/*
=====================================================
🧹 LoggingExampleService 주석처리됨
=====================================================

복잡한 로깅 예제 서비스를 주석처리했습니다.
개발 중에는 간단한 로그만 사용하고,
필요시 주석을 해제하여 상세한 로깅 예제를 활용할 수 있습니다.

=====================================================
*/

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 로깅 사용법 예제 서비스 (주석처리됨)
 * 실제 운영에서 어떻게 로그를 사용하는지 예제를 제공
 */
@Service
@Slf4j
public class LoggingExampleService {
    
    // 🧹 주석처리: 복잡한 로깅 예제 대신 간단하게
    @Autowired
    private BusinessLogger businessLogger;

    /**
     * 🧹 간단한 테스트 메서드
     */
    public void simpleLogTest() {
        log.info("간단한 로그 테스트");
        businessLogger.logReservationCreate(123L, 456L, 1L, 50000, "테스트 예약");
    }

    // ========================================================================================
    // 🗂️ 아래는 모두 주석처리된 복잡한 로깅 예제들
    // ========================================================================================
    
    /*
    예약 생성 예제 - 성공 케이스
    public void createReservationExample() {
        Long userId = 12345L;
        Long reservationId = 67890L;
        
        try {
            // MDC에 컨텍스트 설정
            MDC.put("userId", userId.toString());
            MDC.put("reservationId", reservationId.toString());
            
            log.info("🎯 예약 생성 시작: userId={}", userId);
            
            // 1. 입력 검증
            log.debug("입력 데이터 검증 중...");
            validateReservationData();
            
            // 2. 카테고리 확인
            log.debug("서비스 카테고리 확인 중... categoryId=3");
            Long categoryId = 3L;
            
            // 3. 가격 계산
            log.debug("서비스 가격 계산 중...");
            int calculatedAmount = calculateServiceAmount(categoryId);
            log.info("💰 계산된 서비스 가격: {}원", calculatedAmount);
            
            // 4. 예약 저장
            log.debug("예약 정보 DB 저장 중...");
            saveReservation(userId, categoryId, calculatedAmount);
            
            // 5. 비즈니스 로그 기록
            businessLogger.logReservationCreate(userId, reservationId, categoryId, calculatedAmount, "앱에서 생성");
            
            log.info("✅ 예약 생성 완료: reservationId={}", reservationId);
            
        } catch (Exception e) {
            log.error("❌ 예약 생성 실패: userId={}", userId, e);
            businessLogger.logBusinessError("RESERVATION_CREATE", userId, reservationId, 
                "예약 생성 중 에러 발생: " + e.getMessage(), e);
        } finally {
            MDC.clear();
        }
    }
    
    매칭 시스템 예제 - 실패 케이스
    public void matchingFailedExample() {
        Long reservationId = 67890L;
        
        try {
            MDC.put("reservationId", reservationId.toString());
            
            log.info("🔍 매칭 시작: reservationId={}", reservationId);
            
            // 1. 예약 정보 조회
            log.debug("예약 정보 조회 중...");
            // Reservation reservation = getReservation(reservationId);
            
            // 2. 가능한 매니저 검색
            log.debug("가능한 매니저 검색 중...");
            // List<Manager> availableManagers = findAvailableManagers(reservation);
            
            // 3. 매니저가 없어서 매칭 실패
            log.warn("⚠️ 매칭 실패: 가능한 매니저가 없습니다");
            // businessLogger.logMatchingFailed(reservationId, "가능한 매니저 없음", reservation.getUserId());
            
            // 4. 사용자에게 알림 전송
            log.info("📢 매칭 실패 알림 전송 중...");
            // sendMatchingFailedNotification(reservation.getUserId());
            
        } catch (Exception e) {
            log.error("❌ 매칭 프로세스 에러: reservationId={}", reservationId, e);
            businessLogger.logMatchingFailed(reservationId, e.getMessage(), null);
        } finally {
            MDC.clear();
        }
    }
    
    결제 처리 예제 - 에러 핸들링
    public void paymentProcessExample() {
        Long userId = 12345L;
        Long reservationId = 67890L;
        String payMethod = "CARD";
        int amount = 50000;
        
        try {
            MDC.put("userId", userId.toString());
            MDC.put("reservationId", reservationId.toString());
            
            log.info("💳 결제 시작: userId={}, amount={}원, method={}", userId, amount, payMethod);
            
            // 1. 결제 시작 로그
            businessLogger.logPaymentStart(userId, reservationId, payMethod, amount);
            
            // 2. 결제 처리 시뮬레이션
            log.debug("외부 결제 API 호출 중...");
            Thread.sleep(2000); // API 호출 시뮬레이션
            
            // 3. 결제 성공 (예제에서는 랜덤으로 실패)
            if (Math.random() > 0.7) {
                log.info("✅ 결제 성공: paymentId=PAY123456");
                businessLogger.logPaymentSuccess(userId, reservationId, 123456L, amount);
            } else {
                // 결제 실패 시뮬레이션
                String errorCode = "CARD_DECLINED";
                String errorMsg = "카드가 거절되었습니다";
                
                log.error("❌ 결제 실패: errorCode={}, errorMsg={}", errorCode, errorMsg);
                businessLogger.logPaymentFailed(userId, reservationId, payMethod, amount, 
                    errorCode, errorMsg);
            }
            
        } catch (Exception e) {
            log.error("❌ 결제 처리 중 시스템 에러: userId={}", userId, e);
            businessLogger.logBusinessError("PAYMENT_PROCESS", userId, reservationId, 
                "결제 처리 중 시스템 에러: " + e.getMessage(), e);
        } finally {
            MDC.clear();
        }
    }
    
    성능 테스트 예제 - 느린 메서드
    public void slowMethodExample() {
        long startTime = System.currentTimeMillis();
        
        try {
            log.info("🐌 의도적으로 느린 메서드 시작...");
            
            // 4초 대기 (성능 모니터링 테스트용)
            Thread.sleep(4000);
            
            log.info("작업 완료!");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("작업이 중단되었습니다", e);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("⏱️ 메서드 실행 시간: {}ms", duration);
        }
    }
    
    복잡한 비즈니스 플로우 예제
    public void complexBusinessFlowExample() {
        String requestId = java.util.UUID.randomUUID().toString().substring(0, 8);
        MDC.put("requestId", requestId);
        
        try {
            log.info("🎬 복잡한 비즈니스 플로우 시작 [{}]", requestId);
            
            // Step 1: 사용자 인증
            log.debug("Step 1: 사용자 인증 확인 중...");
            Thread.sleep(100);
            
            // Step 2: 권한 확인
            log.debug("Step 2: 권한 확인 중...");
            Thread.sleep(150);
            
            // Step 3: 데이터 검증
            log.debug("Step 3: 입력 데이터 검증 중...");
            Thread.sleep(200);
            
            // Step 4: 비즈니스 로직 처리
            log.debug("Step 4: 핵심 비즈니스 로직 처리 중...");
            Thread.sleep(500);
            
            // Step 5: 외부 API 호출
            log.debug("Step 5: 외부 API 호출 중...");
            Thread.sleep(800);
            
            // Step 6: 결과 저장
            log.debug("Step 6: 결과 DB 저장 중...");
            Thread.sleep(300);
            
            log.info("✅ 복잡한 비즈니스 플로우 완료 [{}]", requestId);
            
        } catch (Exception e) {
            log.error("❌ 비즈니스 플로우 실패 [{}]", requestId, e);
        } finally {
            MDC.clear();
        }
    }
    
    헬퍼 메서드들 (더미 구현)
    private void validateReservationData() {
        // 검증 로직
    }
    
    private int calculateServiceAmount(Long categoryId) {
        return 50000; // 더미 가격
    }
    
    private void saveReservation(Long userId, Long categoryId, int amount) {
        // DB 저장 로직
    }
    */
} 