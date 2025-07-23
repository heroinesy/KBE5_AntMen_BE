package com.antmen.antwork.common.domain.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

import java.io.IOException;

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
                        .errorCode("INVALID_ARGUMENT")
                        .errorMessage(e.getMessage())
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
                        .errorCode("INVALID_STATE")
                        .errorMessage(e.getMessage())
                        .build());
    }

    /**
     * 그 외 모든 예외 처리 (서버 내부 오류)
     * - 예상하지 못한 모든 예외를 포괄적으로 처리
     * - HTTP 상태코드: 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(HttpServletRequest request, Exception e) {

        log.error("[서버 내부 오류]", e);

        String acceptHeader = request.getHeader("Accept");

        // SSE 연결의 경우: 응답 body를 쓰면 안 됨
        if (acceptHeader != null && acceptHeader.contains("text/event-stream")) {
            log.warn("🔌 SSE 연결 중 예외 발생. 응답 body 없이 종료. message={}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDto.builder()
                        .errorCode("INTERNAL_ERROR")
                        .errorMessage("서버 내부 오류가 발생했습니다.")
                        .build());
    }

    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public ResponseEntity<Void> handleBrokenPipe(Exception e) {
        if (e.getMessage() != null && e.getMessage().contains("Broken pipe")) {
            log.debug("Broken pipe 무시: {}", e.getMessage());
            return ResponseEntity.ok().build();
        }

        log.error("Async 예외", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Void> handleIOException(IOException e) {
        if (e.getMessage() != null && e.getMessage().contains("Broken pipe")) {
            log.debug("Broken pipe 무시(IOException): {}", e.getMessage());
            return ResponseEntity.ok().build();
        }
        log.error("IOException 처리되지 않은 예외", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
