package com.hunko.missionmatching.helper;

import com.hunko.missionmatching.core.scheduler.MissionSchedulerWal;
import com.hunko.missionmatching.core.scheduler.WalMissionDto;
import java.util.List;

public class StubFailMissionWal implements MissionSchedulerWal {
    
    @Override
    public void addLog(WalMissionDto walMissionDto) {
        
    }

    @Override
    public List<WalMissionDto> readAll() {
        return List.of();
    }
}
