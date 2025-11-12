package com.hunko.missionmatching.helper;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.storage.MissionCursor;
import com.hunko.missionmatching.core.domain.MissionReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FakeMissionReader extends MissionReader {
    private final List<Mission> onGoingMissions = new ArrayList<>();
    private final List<Mission> pandingMissions = new ArrayList<>();
    private final List<Mission> completedMissions = new ArrayList<>();

    public FakeMissionReader() {
        super(null);
    }

    public final void addMission(Mission... missions) {
        for (Mission mission : missions) {
            switch (mission.getStatus()) {
                case PENDING:
                    pandingMissions.add(mission);
                    break;
                case COMPLETED:
                    completedMissions.add(mission);
                    break;
                default:
                    onGoingMissions.add(mission);
            }
        }
    }


    @Override
    public List<Mission> readOngoingMission(MissionCursor missionCursor) {
        if (missionCursor.isFirst()) {
            return getMissions(onGoingMissions, null, missionCursor.limit());
        }
        Optional<Mission> mission = getMissions(onGoingMissions, missionCursor.id());
        if (mission.isEmpty()) {
            return Collections.emptyList();
        }
        return getMissions(onGoingMissions, mission.get(), missionCursor.limit());
    }

    @Override
    public List<Mission> readPendingMission(MissionCursor missionCursor) {
        if(missionCursor.isFirst()) {
            return getMissions(pandingMissions, null, missionCursor.limit());
        }
        Optional<Mission> mission = getMissions(pandingMissions, missionCursor.id());
        if (mission.isEmpty()) {
            return Collections.emptyList();
        }
        return getMissions(pandingMissions, mission.get(), missionCursor.limit());
    }

    @Override
    public List<Mission> readCompletedMission(MissionCursor missionCursor) {
        if(missionCursor.isFirst()) {
            return getMissions(completedMissions, null, missionCursor.limit());
        }
        Optional<Mission> mission = getMissions(completedMissions, missionCursor.id());
        if (mission.isEmpty()) {
            return Collections.emptyList();
        }
        return getMissions(completedMissions, mission.get(), missionCursor.limit());
    }

    private Optional<Mission> getMissions(List<Mission> missions, Long id) {
        return missions.stream().filter(mission -> mission.getId().equals(id)).findAny();
    }

    private List<Mission> getMissions(List<Mission> missions, Mission cursorMission, int limit) {
        int index = cursorMission == null ? -1 : missions.indexOf(cursorMission);
        return missions.subList(index + 1,
                Math.min(missions.size(), index + limit));
    }

    @Override
    public Optional<Mission> readById(Long id) {
        Optional<Mission> missions = getMissions(onGoingMissions, id);
        if (missions.isPresent()) {
            return missions;
        }
        missions = getMissions(pandingMissions, id);
        if(missions.isPresent()) {
            return missions;
        }
        return getMissions(completedMissions, id);
    }
}
