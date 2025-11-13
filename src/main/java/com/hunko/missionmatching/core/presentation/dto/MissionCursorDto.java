package com.hunko.missionmatching.core.presentation.dto;

import com.hunko.missionmatching.core.domain.MissionStatus;
import com.hunko.missionmatching.storage.MissionCursor;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record MissionCursorDto(
        Long id,
        MissionStatus status,
        LocalDateTime start,
        LocalDateTime end,
        ZoneId zone
) {
    public MissionCursor toMissionCursor(int limit) {
        if (id == null) {
            return MissionCursor.empty(limit);
        }
        return new MissionCursor(id, status, start.atZone(zone), end.atZone(zone), limit);
    }
}
