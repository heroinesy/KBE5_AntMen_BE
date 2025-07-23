package com.antmen.antwork.common.domain.exception;

import com.antmen.antwork.common.api.controller.BoardController;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(basePackageClasses = BoardController.class)
public class BoardExceptionHandler {
}
