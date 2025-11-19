package com.hunko.missionmatching.core.presentation.controller;

import com.hunko.missionmatching.core.exception.CoreException;
import com.hunko.missionmatching.core.exception.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ErrorResponse> handleCoreException(CoreException e) {
        ErrorType errorType = e.getErrorType();
        log.error(errorType.name(), e);
        return new ResponseEntity<>(ErrorResponse.of(errorType), errorType.getHttpStatus());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ErrorResponse {
        private Integer code;
        private String message;

        public static ErrorResponse of(ErrorType errorType) {
            return new ErrorResponse(errorType.getHttpStatus().value(), errorType.getMessage());
        }
    }
}
