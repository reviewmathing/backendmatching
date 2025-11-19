package com.hunko.missionmatching.core.presentation.dto;

import com.hunko.missionmatching.core.domain.Mission;

public class MissionDtoMapper {
    private MissionDtoMapper() {

    }

    public static MissionDto toDto(Mission mission, boolean requested) {
        return new MissionDto(
                mission.getId().toLong(),
                mission.getTitle(),
                mission.getTimePeriod().getOriginStartDate(),
                mission.getTimePeriod().getOriginEndDate(),
                mission.getTimePeriod().getZoneId(),
                mission.getStatus(),
                requested
        );
    }
}
