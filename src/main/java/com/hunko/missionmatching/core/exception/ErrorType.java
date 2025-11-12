package com.hunko.missionmatching.core.exception;

public enum ErrorType {
    INVALID_INPUT(""),
    ENTITY_NOT_FOUND(""),;

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return null;
    }

    public void throwException() {
        throw new CoreException(this);
    }

    public CoreException toException() {
        return new CoreException(this);
    }
}
