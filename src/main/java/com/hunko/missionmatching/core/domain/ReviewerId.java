package com.hunko.missionmatching.core.domain;

public record ReviewerId(Long id) {

    public static  ReviewerId of(Long id) {
        return new ReviewerId(id);
    }

    public Long toLong() {
        return this.id;
    }
}
