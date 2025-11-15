package com.hunko.missionmatching.core.domain;

public record RevieweeId(Long id) {

    public static RevieweeId of(Long id) {
        return new RevieweeId(id);
    }

    public Long toLong() {
        return this.id;
    }
}
