package com.antmen.antwork.common.dto;



@Builder
@Getter
public class ErrorResponseDto {
    private String errorCode;        // 에러코드
    private String errorMessage;     // 사용자에게 전달하는 에러 메세지
}
