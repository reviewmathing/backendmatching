package com.hunko.missionmatching.core.exception;

import lombok.Getter;

@Getter
public class CoreException extends RuntimeException {

    private final ErrorType errorType;

    public CoreException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public CoreException(ErrorType errorType, Throwable cause) {
        super(cause);
        this.errorType = errorType;
    }
}
