package com.hunko.missionmatching.core.domain;

public record ReviewRequestId(Long id) {

    public static ReviewRequestId empty() {
        return new ReviewRequestId(null);
    }

    public static ReviewRequestId of(Long id) {
        return new ReviewRequestId(id);
    }

    public Long toLong() {
        return Long.valueOf(id);
    }
}
