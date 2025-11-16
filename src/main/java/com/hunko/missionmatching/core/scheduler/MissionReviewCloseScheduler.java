package com.hunko.missionmatching.core.scheduler;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.ReviewLimitTimeCalcService;
import com.hunko.missionmatching.util.DateUtil;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MissionReviewCloseScheduler extends MissionScheduler {

    private final ReviewLimitTimeCalcService reviewLimitTimeCalcService;
    private final JobLauncher jobLauncher;
    private final Job assignmentCloseJob;

    @Override
    protected LocalDateTime getScheduleTime(Mission mission) {
        ZonedDateTime limitZoneDateTime = reviewLimitTimeCalcService.calc(mission.getTimePeriod().getEndDate());
        return DateUtil.toServerDateTime(limitZoneDateTime);
    }

    @Override
    protected void handle(Long id, LocalDateTime time) {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addLong("missionId", id);
        try {
            jobLauncher.run(assignmentCloseJob, jobParametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            log.error(e.getMessage(), e);
        }
    }
}
