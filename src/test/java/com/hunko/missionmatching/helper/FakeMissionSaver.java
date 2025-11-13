package com.hunko.missionmatching.helper;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.MissionId;
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
        Mission copyMission = new Mission(
                mission.getId().toLong() == null ? MissionId.of((long) missions.size()) : mission.getId(),
                mission.getTitle(),
                mission.getTimePeriod(),
                mission.getCreator(),
                mission.getStatus(),
                mission.getMissionUrl()
        );
        missions.add(copyMission);
        return copyMission.getId().toLong();
    }

    public Mission getMission(int id) {
        return missions.stream().filter(mission -> mission.getId().toLong() == id).findFirst().orElse(null);
    }
}
