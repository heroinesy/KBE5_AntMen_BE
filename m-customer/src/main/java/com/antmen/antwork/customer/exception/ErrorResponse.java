package com.antmen.antwork.customer.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private String message;
    private int status;
    private String errorCode;
}

