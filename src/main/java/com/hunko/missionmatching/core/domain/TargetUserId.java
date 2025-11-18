package com.hunko.missionmatching.core.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TargetUserId {

    private final Long userId;

    public static TargetUserId of(Long userId) {
        return new TargetUserId(userId);
    }

    public Long toLong() {
        return userId;
    }
}
