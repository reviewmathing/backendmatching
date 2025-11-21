package com.hunko.missionmatching.core.job;

import static org.assertj.core.api.Assertions.assertThat;

import com.hunko.missionmatching.core.domain.GithubUri;
import com.hunko.missionmatching.core.domain.ReviewRequestType;
import com.hunko.missionmatching.core.domain.UserReader;
import com.hunko.missionmatching.core.scheduler.MissionSchedulerWalFactory;
import com.hunko.missionmatching.helper.StubFailMissionWalFactory;
import com.hunko.missionmatching.storage.ReviewRequestEntity;
import com.hunko.missionmatching.storage.ReviewRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@SpringBatchTest
class RejectJobTest {

    @Autowired
    private ReviewRequestRepository reviewRequestRepository;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job updateRejectJob;

    @TestBean
    private MissionSchedulerWalFactory missionWalFactory;

    static MissionSchedulerWalFactory missionWalFactory() {
        return new StubFailMissionWalFactory();
    }

    @MockitoBean
    private UserReader userReader;

    @BeforeEach
    void setUp() {
        reviewRequestRepository.deleteAll();
    }

    @Test
    void 배치성공()
            throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        ReviewRequestEntity urlEntity = reviewRequestRepository.save(
                new ReviewRequestEntity(
                        null,
                        1L,
                        1L,
                        3,
                        GithubUri.of("http://github.com").toUriString(),
                        ReviewRequestType.REQUEST
                )
        );
        ReviewRequestEntity nonHaveUrlEntity = reviewRequestRepository.save(
                new ReviewRequestEntity(
                        null,
                        1L,
                        2L,
                        3,
                        null,
                        ReviewRequestType.REQUEST
                )
        );

        JobParameters params = new JobParametersBuilder()
                .addLong("missionId", 1L)
                .toJobParameters();

        jobLauncher.run(updateRejectJob, params);

        assertThat(reviewRequestRepository.findById(urlEntity.getId()).get()
                .getReviewRequestType()).isEqualTo(ReviewRequestType.REQUEST);
        assertThat(reviewRequestRepository.findById(nonHaveUrlEntity.getId()).get()
                .getReviewRequestType()).isEqualTo(ReviewRequestType.REJECT);
    }
}
