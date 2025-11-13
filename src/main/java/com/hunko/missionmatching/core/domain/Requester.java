package com.hunko.missionmatching.core.domain;

public record Requester(Long id) {

    public static Requester of(Long id) {
        return new Requester(id);
    }

    public Long toLong() {
        return id;
    }
}
