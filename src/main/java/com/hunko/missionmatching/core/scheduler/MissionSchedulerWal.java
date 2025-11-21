package com.hunko.missionmatching.core.scheduler;

import java.util.List;

public interface MissionSchedulerWal {
    void addLog(WalMissionDto walMissionDto);

    List<WalMissionDto> readAll();
}
