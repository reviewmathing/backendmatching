package com.hunko.missionmatching.core.scheduler;

import com.hunko.missionmatching.core.application.service.MissionService;
import com.hunko.missionmatching.core.domain.Mission;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MissionStartedScheduler extends MissionScheduler {

    private final MissionService missionService;

    public MissionStartedScheduler(@Value("${scheduler.log.path}") String path, MissionService missionService) {
        super(path, "missionStartedScheduler");
        this.missionService = missionService;
    }

    @Override
    protected void handle(Long id, LocalDateTime startedTime) {
        missionService.updateOngoing(id, startedTime);
    }

    @Override
    protected LocalDateTime getScheduleTime(Mission mission) {
        return mission.getTimePeriod().getServerStartDateTime();
    }
}
