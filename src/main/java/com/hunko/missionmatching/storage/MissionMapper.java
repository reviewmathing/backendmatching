package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.Creator;
import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.TimePeriod;

public class MissionMapper {

    public static MissionEntity toMissionEntity(Mission mission) {
        TimePeriod timePeriod = mission.getTimePeriod();
        return MissionEntity.builder()
                .id(mission.getId())
                .title(mission.getTitle())
                .startDate(timePeriod.getStartDate())
                .endDate(timePeriod.getEndDate())
                .creator(mission.getCreator().toLong())
                .status(mission.getStatus())
                .build();

    }

    public static Mission toMission(MissionEntity entity) {
        return new Mission(
                entity.getId(),
                entity.getTitle(),
                new TimePeriod(entity.getStartDate(), entity.getEndDate()),
                Creator.of(entity.getCreator()),
                entity.getStatus()
        );
    }
}
