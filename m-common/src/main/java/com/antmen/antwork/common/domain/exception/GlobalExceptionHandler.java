package com.antmen.antwork.common.domain.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 잘못된 요청 처리 - 클라이언트가 잘못된 파라미터를 전달한 경우
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException e) {

        log.warn("[잘못된 요청] {}", e.getMessage());

        return ResponseEntity.badRequest()
                .body(ErrorResponseDto.builder()
                        .code("INVALID_ARGUMENT")
                        .message(e.getMessage())
                        .build());
    }

    /**
     * 비즈니스 로직 상의 상태 오류 처리
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalState(IllegalStateException e) {

        log.warn("[상태 오류] {}", e.getMessage());

        return ResponseEntity.badRequest()
                .body(ErrorResponseDto.builder()
                        .code("INVALID_STATE")
                        .message(e.getMessage())
                        .build());
    }

    /**
     * 그 외 모든 예외 처리 (서버 내부 오류)
     * - 예상하지 못한 모든 예외를 포괄적으로 처리
     * - HTTP 상태코드: 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception e) {

        log.error("[서버 내부 오류]", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDto.builder()
                        .code("INTERNAL_ERROR")
                        .message("서버 내부 오류가 발생했습니다.")
                        .build());
    }
}
