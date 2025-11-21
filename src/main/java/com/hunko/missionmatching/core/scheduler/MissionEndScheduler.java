package com.hunko.missionmatching.core.scheduler;

import com.hunko.missionmatching.core.application.service.MissionService;
import com.hunko.missionmatching.core.domain.Mission;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MissionEndScheduler extends MissionScheduler {

    @Autowired
    private final MissionService missionService;

    public MissionEndScheduler(MissionService missionService, MissionSchedulerWalFactory factory) {
        super(factory.create("endSchedule"));
        this.missionService = missionService;
    }

    @Override
    protected LocalDateTime getScheduleTime(Mission mission) {
        return mission.getTimePeriod().getServerEndDateTime();
    }

    @Override
    protected void handle(Long id, LocalDateTime endTime) {
        missionService.updateCompleted(id, endTime);
    }
}
