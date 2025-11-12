package com.hunko.missionmatching.core.presentation;

import com.hunko.missionmatching.core.domain.MissionStatus;
import com.hunko.missionmatching.storage.MissionCursor;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.RequestParam;

public record MissionCursorDto(
        Long id,
        MissionStatus status,
        LocalDateTime start,
        LocalDateTime end
) {
    public MissionCursor toMissionCursor(int limit) {
        if(id == null) {
            return MissionCursor.empty(limit);
        }
        return new MissionCursor(id, status, start, end, limit);
    }
}
