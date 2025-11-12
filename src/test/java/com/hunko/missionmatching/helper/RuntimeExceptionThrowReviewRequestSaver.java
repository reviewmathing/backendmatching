package com.hunko.missionmatching.helper;

import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.domain.ReviewRequestSaver;

public class RuntimeExceptionThrowReviewRequestSaver extends ReviewRequestSaver {

    private final RuntimeException exception;

    public RuntimeExceptionThrowReviewRequestSaver(RuntimeException exception) {
        super(null);
        this.exception = exception;
    }

    @Override
    public Long save(ReviewRequest request) {
        throw exception;
    }
}
