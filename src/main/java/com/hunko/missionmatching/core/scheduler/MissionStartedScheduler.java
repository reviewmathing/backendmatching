package com.hunko.missionmatching.core.scheduler;

import com.hunko.missionmatching.core.application.service.MissionService;
import com.hunko.missionmatching.core.domain.Mission;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MissionStartedScheduler extends MissionScheduler {

    private final MissionService missionService;

    @Override
    protected void handle(Long id, LocalDateTime startedTime) {
        missionService.updateOngoing(id, startedTime);
    }

    @Override
    protected LocalDateTime getScheduleTime(Mission mission) {
        return mission.getTimePeriod().getServerStartDateTime();
    }
}
