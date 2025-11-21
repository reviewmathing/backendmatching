package com.hunko.missionmatching.core.presentation.dto;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.MissionStatus;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record MissionDetailDto(Long id, String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
                               ZoneId zone,
                               MissionStatus missionStatus, String githubUri) {

    public static MissionDetailDto from(Mission mission) {
        return new MissionDetailDto(
                mission.getId().toLong(),
                mission.getTitle(),
                mission.getTimePeriod().getOriginStartDate(),
                mission.getTimePeriod().getOriginEndDate(),
                mission.getTimePeriod().getZoneId(),
                mission.getStatus(),
                mission.getMissionUrl().toUriString()
        );
    }
}
