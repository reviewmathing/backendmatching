package com.hunko.missionmatching.helper;

import com.hunko.missionmatching.core.scheduler.MissionSchedulerWal;
import com.hunko.missionmatching.core.scheduler.MissionSchedulerWalFactory;

public class StubFailMissionWalFactory extends MissionSchedulerWalFactory {
    public StubFailMissionWalFactory() {
        super(null);
    }

    @Override
    public MissionSchedulerWal create(String name) {
        return new StubFailMissionWal();
    }
}
