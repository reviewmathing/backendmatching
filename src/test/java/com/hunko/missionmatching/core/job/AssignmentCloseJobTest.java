package com.hunko.missionmatching.core.job;

import static org.assertj.core.api.Assertions.assertThat;

import com.hunko.missionmatching.core.domain.GithubUri;
import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import com.hunko.missionmatching.core.domain.ReviewRequestType;
import com.hunko.missionmatching.storage.ReviewAssignmentEntity;
import com.hunko.missionmatching.storage.ReviewAssignmentRepository;
import com.hunko.missionmatching.storage.ReviewRequestEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBatchTest
class AssignmentCloseJobTest {

    @Autowired
    private ReviewAssignmentRepository reviewAssignmentRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job assignmentCloseJob;

    @BeforeEach
    void setUp() {
        reviewAssignmentRepository.deleteAll();
    }

    @Test
    void 배치성공()
            throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        for(int i = 0; i < 10; i++) {
            ReviewAssignmentEntity reviewAssignmentEntity = new ReviewAssignmentEntity(
                    null,
                    1L,
                    (long)i,
                    ZonedDateTime.now(),
                    ReviewAssignmentStatus.NOT_CLEARED
            );
            reviewAssignmentRepository.save(reviewAssignmentEntity);
        }

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addLong("missionId",1L);
        jobLauncher.run(assignmentCloseJob,jobParametersBuilder.toJobParameters());

        TypedQuery<ReviewAssignmentEntity> query = entityManager.createQuery(
                "select r from ReviewAssignmentEntity r where r.reviewAssignmentStatus = 'NOT_CLEARED'",
                ReviewAssignmentEntity.class);
        List<ReviewAssignmentEntity> resultList = query.getResultList();
        assertThat(resultList).isEmpty();
    }
}
