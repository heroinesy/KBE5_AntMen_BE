package com.antmen.antwork.common.util.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/*
==========================================================
🧹 BusinessLogger 주석처리됨 (복잡한 로그 대신 간단하게)
==========================================================

이 클래스는 복잡한 비즈니스 로그 추적을 위한 전용 로거였지만,
개발 편의성을 위해 주석처리되었습니다.

필요시 주석을 해제하여 사용할 수 있습니다.
==========================================================
*/

/**
 * 비즈니스 로직 추적을 위한 전용 로거
 * 예약, 매칭, 결제 등 핵심 비즈니스 플로우를 추적
 */
@Component
@Slf4j
public class BusinessLogger {
    
    // 🧹 주석처리: 복잡한 로그 대신 간단한 개발을 위해
    /*
    private static final Logger BUSINESS_LOGGER = LoggerFactory.getLogger("BUSINESS_LOG");
    
    // 비즈니스 액션 상수
    public static final String ACTION_RESERVATION_CREATE = "RESERVATION_CREATE";
    public static final String ACTION_RESERVATION_CANCEL = "RESERVATION_CANCEL";
    public static final String ACTION_MATCHING_START = "MATCHING_START";
    public static final String ACTION_MATCHING_SUCCESS = "MATCHING_SUCCESS";
    public static final String ACTION_MATCHING_FAILED = "MATCHING_FAILED";
    public static final String ACTION_MATCHING_ACCEPT = "MATCHING_ACCEPT";
    public static final String ACTION_MATCHING_REJECT = "MATCHING_REJECT";
    public static final String ACTION_PAYMENT_START = "PAYMENT_START";
    public static final String ACTION_PAYMENT_SUCCESS = "PAYMENT_SUCCESS";
    public static final String ACTION_PAYMENT_FAILED = "PAYMENT_FAILED";
    public static final String ACTION_REFUND_REQUEST = "REFUND_REQUEST";
    public static final String ACTION_USER_LOGIN = "USER_LOGIN";
    public static final String ACTION_USER_SIGNUP = "USER_SIGNUP";
    */

    /**
     * 🧹 간단한 로그 메서드 (BusinessLogger 호출 부분을 대체)
     */
    public void logReservationCreate(Long userId, Long reservationId, Long categoryId, int amount, String memo) {
        log.info("예약 생성: userId={}, reservationId={}, categoryId={}, amount={}", 
            userId, reservationId, categoryId, amount);
    }
    
    public void logReservationCancel(Long userId, Long reservationId, String reason) {
        log.info("예약 취소: userId={}, reservationId={}, reason={}", userId, reservationId, reason);
    }
    
    public void logMatchingStart(Long reservationId, int availableManagerCount, double searchRadius) {
        log.info("매칭 시작: reservationId={}, availableManagers={}", reservationId, availableManagerCount);
    }
    
    public void logMatchingSuccess(Long reservationId, Long managerId, int priority) {
        log.info("매칭 성공: reservationId={}, managerId={}", reservationId, managerId);
    }
    
    public void logPaymentSuccess(Long userId, Long reservationId, Long paymentId, int amount) {
        log.info("결제 성공: userId={}, reservationId={}, paymentId={}, amount={}", 
            userId, reservationId, paymentId, amount);
    }
    
    // 기타 필요한 로그 메서드들도 간단한 버전으로...
    public void logUserLogin(Long userId, String loginMethod, String clientIp) {
        log.info("사용자 로그인: userId={}, method={}", userId, loginMethod);
    }

    // ========================================================================================
    // 🗂️ 아래는 모두 주석처리된 복잡한 로그 메서드들
    // ========================================================================================
    
    /*
    예약 생성 로그
    public void logReservationCreate(Long userId, Long reservationId, Long categoryId, int amount, String memo) {
        setBusinessContext(ACTION_RESERVATION_CREATE, userId, reservationId);
        BUSINESS_LOGGER.info("사용자 예약 생성: categoryId={} amount={} memo='{}'", 
            categoryId, amount, memo);
    }
    
    예약 취소 로그
    public void logReservationCancel(Long userId, Long reservationId, String reason) {
        setBusinessContext(ACTION_RESERVATION_CANCEL, userId, reservationId);
        BUSINESS_LOGGER.info("사용자 예약 취소: reason='{}'", reason);
    }
    
    매칭 시작 로그
    public void logMatchingStart(Long reservationId, int availableManagerCount, double searchRadius) {
        setBusinessContext(ACTION_MATCHING_START, null, reservationId);
        BUSINESS_LOGGER.info("매칭 시작: availableManagers={} searchRadius={}km", 
            availableManagerCount, searchRadius);
    }
    
    매칭 성공 로그
    public void logMatchingSuccess(Long reservationId, Long managerId, int priority) {
        setBusinessContext(ACTION_MATCHING_SUCCESS, null, reservationId);
        BUSINESS_LOGGER.info("매칭 성공: managerId={} priority={}", managerId, priority);
    }
    
    매칭 실패 로그
    public void logMatchingFailed(Long reservationId, String reason, Long userId) {
        setBusinessContext(ACTION_MATCHING_FAILED, userId, reservationId);
        BUSINESS_LOGGER.warn("매칭 실패: reason='{}'", reason);
    }
    
    매니저 매칭 수락 로그
    public void logMatchingAccept(Long userId, Long reservationId, Long matchingId) {
        setBusinessContext(ACTION_MATCHING_ACCEPT, userId, reservationId);
        BUSINESS_LOGGER.info("매니저 매칭 수락: matchingId={}", matchingId);
    }
    
    매니저 매칭 거절 로그
    public void logMatchingReject(Long userId, Long reservationId, Long matchingId, String reason) {
        setBusinessContext(ACTION_MATCHING_REJECT, userId, reservationId);
        BUSINESS_LOGGER.info("매니저 매칭 거절: matchingId={} reason='{}'", matchingId, reason);
    }
    
    결제 시작 로그
    public void logPaymentStart(Long userId, Long reservationId, String payMethod, int amount) {
        setBusinessContext(ACTION_PAYMENT_START, userId, reservationId);
        BUSINESS_LOGGER.info("결제 시작: payMethod={} amount={}", payMethod, amount);
    }
    
    결제 성공 로그
    public void logPaymentSuccess(Long userId, Long reservationId, Long paymentId, int amount) {
        setBusinessContext(ACTION_PAYMENT_SUCCESS, userId, reservationId);
        BUSINESS_LOGGER.info("결제 성공: paymentId={} amount={}", paymentId, amount);
    }
    
    결제 실패 로그
    public void logPaymentFailed(Long userId, Long reservationId, String payMethod, int amount, String errorCode, String errorMsg) {
        setBusinessContext(ACTION_PAYMENT_FAILED, userId, reservationId);
        BUSINESS_LOGGER.error("결제 실패: payMethod={} amount={} errorCode={} errorMsg='{}'", 
            payMethod, amount, errorCode, errorMsg);
    }
    
    환불 요청 로그
    public void logRefundRequest(Long userId, Long reservationId, Long paymentId, String reason) {
        setBusinessContext(ACTION_REFUND_REQUEST, userId, reservationId);
        BUSINESS_LOGGER.info("환불 요청: paymentId={} reason='{}'", paymentId, reason);
    }
    
    사용자 로그인 로그
    public void logUserLogin(Long userId, String loginMethod, String clientIp) {
        setBusinessContext(ACTION_USER_LOGIN, userId, null);
        BUSINESS_LOGGER.info("사용자 로그인: method={} clientIp={}", loginMethod, clientIp);
    }
    
    사용자 회원가입 로그
    public void logUserSignup(Long userId, String signupMethod, String userType) {
        setBusinessContext(ACTION_USER_SIGNUP, userId, null);
        BUSINESS_LOGGER.info("사용자 회원가입: method={} userType={}", signupMethod, userType);
    }
    
    커스텀 비즈니스 로그
    public void logCustomAction(String action, Long userId, Long reservationId, String message, Object... args) {
        setBusinessContext(action, userId, reservationId);
        BUSINESS_LOGGER.info(message, args);
    }
    
    비즈니스 에러 로그
    public void logBusinessError(String action, Long userId, Long reservationId, String errorMessage, Exception e) {
        setBusinessContext(action, userId, reservationId);
        BUSINESS_LOGGER.error("비즈니스 에러: {}", errorMessage, e);
    }
    
    MDC에 비즈니스 컨텍스트 설정
    private void setBusinessContext(String action, Long userId, Long reservationId) {
        MDC.put("businessAction", action);
        if (userId != null) {
            MDC.put("userId", userId.toString());
        }
        if (reservationId != null) {
            MDC.put("reservationId", reservationId.toString());
        }
    }
    
    성능 임계치를 초과한 작업 로그
    public void logSlowBusiness(String action, Long userId, Long reservationId, long duration, String operation) {
        setBusinessContext(action, userId, reservationId);
        BUSINESS_LOGGER.warn("⚠️ 느린 비즈니스 로직: operation='{}' duration={}ms", operation, duration);
    }
    
    중요한 비즈니스 결정 로그
    public void logBusinessDecision(String action, Long userId, Long reservationId, String decision, String reason) {
        setBusinessContext(action, userId, reservationId);
        BUSINESS_LOGGER.info("비즈니스 결정: decision='{}' reason='{}'", decision, reason);
    }
    */
} 