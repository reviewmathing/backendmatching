package com.hunko.missionmatching.core.presentation;

import com.hunko.missionmatching.core.domain.Mission;

public class MissionDtoMapper {
    private MissionDtoMapper() {

    }

    public static MissionDto toDto(Mission mission) {
        return new MissionDto(
                mission.getId(),
                mission.getTitle(),
                mission.getTimePeriod().getStartDate(),
                mission.getTimePeriod().getEndDate(),
                mission.getStatus()
        );
    }
}
