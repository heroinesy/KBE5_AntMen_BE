package com.antmen.antwork.customer.exception;

import com.antmen.antwork.common.domain.exception.ErrorResponseDto;
import com.antmen.antwork.common.domain.exception.UnauthorizedAccessException;
import com.antmen.antwork.customer.controller.CustomerController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = CustomerController.class)
public class CustomerExceptionHandler {

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        ErrorResponseDto response = ErrorResponseDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode("UNAUTHORIZED_ACTION")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }


}
