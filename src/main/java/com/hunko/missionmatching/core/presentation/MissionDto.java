package com.hunko.missionmatching.core.presentation;

import com.hunko.missionmatching.core.domain.MissionStatus;
import java.time.LocalDateTime;

public record MissionDto(Long id, String name, LocalDateTime startDateTime, LocalDateTime endDateTime, MissionStatus missionStatus) {
}
