package com.hunko.missionmatching.core.scheduler;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.ReviewLimitTimeCalcService;
import com.hunko.missionmatching.util.DateUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MissionReviewCloseScheduler extends MissionScheduler{

    private final ReviewLimitTimeCalcService reviewLimitTimeCalcService;

    @Override
    protected LocalDateTime getScheduleTime(Mission mission) {
        ZonedDateTime limitZoneDateTime = reviewLimitTimeCalcService.calc(mission.getTimePeriod().getEndDate());
        return DateUtil.toServerDateTime(limitZoneDateTime);
    }

    @Override
    protected void handle(Long id, LocalDateTime time) {

    }
}
