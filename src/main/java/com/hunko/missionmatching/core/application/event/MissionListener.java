package com.hunko.missionmatching.core.application.event;

import com.hunko.missionmatching.core.application.service.MatchBatchService;
import com.hunko.missionmatching.core.application.service.ReviewRequestService;
import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.MissionCompleted;
import com.hunko.missionmatching.core.domain.MissionOngoinged;
import com.hunko.missionmatching.core.domain.MissionPeriodUpdated;
import com.hunko.missionmatching.core.domain.MissionReader;
import com.hunko.missionmatching.core.domain.MissionRegistered;
import com.hunko.missionmatching.core.domain.ReviewMatched;
import com.hunko.missionmatching.core.scheduler.MissionEndScheduler;
import com.hunko.missionmatching.core.scheduler.MissionReviewCloseScheduler;
import com.hunko.missionmatching.core.scheduler.MissionStartedScheduler;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class MissionListener {

    private final MissionReader missionReader;
    private final MissionStartedScheduler missionStartedScheduler;
    private final MissionEndScheduler missionEndScheduler;
    private final JobLauncher jobLauncher;
    private final Job updateRejectJob;
    private final MatchBatchService matchBatchService;
    private final ReviewRequestService reviewRequestService;
    private final MissionReviewCloseScheduler missionReviewCloseScheduler;

    @TransactionalEventListener(value = MissionRegistered.class, phase = TransactionPhase.AFTER_COMMIT)
    public void handle(MissionRegistered event) {
        missionReader.readById(event.id()).ifPresent(missionStartedScheduler::schedule);
    }

    @TransactionalEventListener(value = MissionPeriodUpdated.class, phase = TransactionPhase.AFTER_COMMIT)
    public void handle(MissionPeriodUpdated event) {
        missionReader.readById(event.id().toLong()).ifPresent(missionStartedScheduler::schedule);
    }

    @TransactionalEventListener(value = MissionOngoinged.class, phase = TransactionPhase.AFTER_COMMIT)
    public void handle(MissionOngoinged event) {
        missionReader.readById(event.id()).ifPresent(missionEndScheduler::schedule);
    }

    @Async("threadPoolTaskExecutor")
    @TransactionalEventListener(value = MissionCompleted.class, phase = TransactionPhase.AFTER_COMMIT)
    public void handle(MissionCompleted event) {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("missionId", event.id())
                    .addLocalDateTime("job time",LocalDateTime.now())
                    .toJobParameters();
            jobLauncher.run(updateRejectJob, params);
            Mission mission = missionReader.readById(event.id()).get();
            matchBatchService.match(mission);
            missionReviewCloseScheduler.schedule(mission);

        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Async("threadPoolTaskExecutor")
    @TransactionalEventListener(value = ReviewMatched.class, phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ReviewMatched event) {
        try {
            reviewRequestService.matched(event.reviewRequestId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
