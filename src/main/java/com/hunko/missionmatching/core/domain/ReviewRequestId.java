package com.hunko.missionmatching.core.domain;

public record ReviewRequestId(Long id) {

    public static ReviewRequestId empty() {
        return new ReviewRequestId(null);
    }
}
