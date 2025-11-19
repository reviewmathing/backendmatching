package com.hunko.missionmatching.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    INVALID_INPUT("잘못된 입력입니다", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND("해당 데이터를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_ACTION("접근 권한이 없습니다", HttpStatus.FORBIDDEN),
    DUPLICATE_REVIEW_REQUEST("중복된 리뷰입니다.", HttpStatus.CONFLICT),
    INVALID_MISSION("잘못된 입력입니다", HttpStatus.BAD_REQUEST),
    NOT_SUB_URL_FOR_MISSION("해당 미션의 URI가 아닙니다", HttpStatus.BAD_REQUEST),
    REVIEW_REQUETST_RULE_VIOLATION("신고 혹은 미완료 리뷰가 누적되어 리뷰신청을 할 수 없습니다", HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_REVIEW_REQUEST_STATE("리뷰의 상태를 변경할 수 없습니다", HttpStatus.CONFLICT),
    INVALID_REVIEW_ASSIGNMENT_STATE("리뷰 그룹의 상태를 변경 할 수 없습니다", HttpStatus.CONFLICT),
    INVALID_MISSION_STATE("미션의 상태를 변경 할 수 없습니다", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorType(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public void throwException() {
        throw new CoreException(this);
    }

    public CoreException toException() {
        return new CoreException(this);
    }
}
