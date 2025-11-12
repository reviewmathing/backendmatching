package com.hunko.missionmatching.core.presentation;

import com.hunko.missionmatching.core.domain.MissionStatus;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record MissionDto(Long id, String name, LocalDateTime startDateTime, LocalDateTime endDateTime, ZoneId zone, MissionStatus missionStatus) {
}
