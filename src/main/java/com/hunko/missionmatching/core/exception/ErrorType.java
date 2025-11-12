package com.hunko.missionmatching.core.exception;

public enum ErrorType {
    INVALID_INPUT(""),
    ENTITY_NOT_FOUND(""),
    DUPLICATE_REVIEW_REQUEST("중복된 리뷰입니다."),
    INVALID_MISSION("잘못된 입력입니다");

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
