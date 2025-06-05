package com.antmen.antwork.common.domain.exception;

import lombok.*;

@Builder
@Getter
public class ErrorResponseDto {
    private String errorCode;        // 에러코드
    private String errorMessage;     // 사용자에게 전달하는 에러 메세지
}
