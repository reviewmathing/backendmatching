package com.hunko.missionmatching.core.domain;

import java.time.LocalDateTime;

public record MissionRegistered(Long id, LocalDateTime eventTime) {
    public MissionRegistered(Long id) {
        this(id, LocalDateTime.now());
    }
}
