package com.hunko.missionmatching.core.scheduler;

public class MissionSchedulerWalFactory {

    private final String path;

    public MissionSchedulerWalFactory(String path) {
        this.path = path;
    }

    public MissionSchedulerWal create(String name) {
        return new FailMissionSchedulerWal(path,name);
    }

}
