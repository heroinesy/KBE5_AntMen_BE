package com.antmen.antwork.common.domain.exception;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private String code;        // 에러코드
    private String message;     // 사용자에게 전달하는 에러 메세지
}
