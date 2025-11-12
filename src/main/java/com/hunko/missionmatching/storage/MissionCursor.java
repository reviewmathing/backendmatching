package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.MissionStatus;
import java.time.LocalDateTime;

public record MissionCursor(Long id, MissionStatus status, LocalDateTime start, LocalDateTime end, int limit) {

    public boolean isFirst() {
        return id == null;
    }

    public static MissionCursor empty(int limit) {
        return new MissionCursor(null, null, null, null, limit);
    }

    public MissionCursor updateLimit(int limit){
        return  new MissionCursor(id, status, start, end, limit);
    }
}
