package com.hunko.missionmatching.helper;


import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.storage.MissionEntity;
import com.hunko.missionmatching.storage.MissionMapper;
import com.hunko.missionmatching.core.domain.MissionSaver;
import java.util.ArrayList;
import java.util.List;

public class FakeMissionSaver extends MissionSaver {

    private final List<Mission> missions = new ArrayList<>();

    public FakeMissionSaver() {
        super(null);
    }

    @Override
    public Long save(Mission mission) {
        MissionEntity missionEntity = MissionMapper.toMissionEntity(mission);
        MissionEntity copy = MissionEntity.builder()
                .id(mission.getId() == null ? missions.size() : mission.getId())
                .title(mission.getTitle())
                .status(mission.getStatus())
                .startDate(missionEntity.getStartDate())
                .endDate(missionEntity.getEndDate())
                .creator(missionEntity.getCreator())
                .build();
        Mission copyMission = MissionMapper.toMission(copy);
        missions.add(copyMission);
        return copyMission.getId();
    }

    public Mission getMission(int id) {
        return missions.stream().filter(mission -> mission.getId() == id).findFirst().orElse(null);
    }
}
