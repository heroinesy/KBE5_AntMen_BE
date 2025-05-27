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
     * NotFoundException (리소스를 찾을 수 없음) 처리
     * - 예: 특정 예약이 존재하지 않을 때
     * - HTTP 상태코드: 404
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException e) {
        log.error("[요청이 없음]", e);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.builder()
                        .code("NOT_FOUND")
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
