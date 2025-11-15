package com.hunko.missionmatching.core.application.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.hunko.missionmatching.core.application.service.ReviewRequestReader;
import com.hunko.missionmatching.core.domain.GithubUri;
import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.MissionCompleted;
import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.MissionSaver;
import com.hunko.missionmatching.core.domain.MissionStatus;
import com.hunko.missionmatching.core.domain.Requester;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.domain.ReviewRequestId;
import com.hunko.missionmatching.core.domain.ReviewRequestSaver;
import com.hunko.missionmatching.core.domain.ReviewRequestType;
import com.hunko.missionmatching.core.domain.TestMissionFactory;
import com.hunko.missionmatching.storage.MissionRepository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@SpringBatchTest
class MissionListenerTest {

    @Autowired
    private MissionListener missionListener;

    @Autowired
    private MissionSaver missionSaver;

    @Autowired
    private ReviewRequestSaver reviewRequestSaver;

    @Autowired
    private ReviewRequestReader reviewRequestReader;

    @Autowired
    private MissionRepository missionRepository;

    @BeforeEach
    void setUp() {
        missionRepository.deleteAll();
    }

    @Test
    void 미션완료시테스트() {
        Mission mission = TestMissionFactory.createMission(null, MissionStatus.COMPLETED, 1L);
        Long missionId = missionSaver.save(mission);

        ReviewRequest request1 = new ReviewRequest(
                Requester.of(1L),
                MissionId.of(missionId),
                3
        );
        Long request1Id = reviewRequestSaver.save(request1);
        ReviewRequest request2 = new ReviewRequest(
                Requester.of(2L),
                MissionId.of(missionId),
                3
        );
        Long request2Id = reviewRequestSaver.save(request2);
        ReviewRequest request3 = new ReviewRequest(
                ReviewRequestId.empty(),
                Requester.of(3L),
                MissionId.of(missionId),
                3,
                GithubUri.of("http://github.com"),
                ReviewRequestType.REQUEST
        );
        Long request3Id = reviewRequestSaver.save(request3);
        ReviewRequest request4 = new ReviewRequest(
                ReviewRequestId.empty(),
                Requester.of(4L),
                MissionId.of(missionId),
                3,
                GithubUri.of("http://github.com"),
                ReviewRequestType.REQUEST
        );
        Long request4Id = reviewRequestSaver.save(request4);

        missionListener.handle(new MissionCompleted(1L));

        await()
                .atMost(1, TimeUnit.MINUTES)
                .untilAsserted(
                        ()->{
                            ReviewRequest reviewRequest1 = reviewRequestReader.loadFrom(
                                    ReviewRequestId.of(request1Id)).get();
                            assertThat(reviewRequest1.getReviewRequestStatus()).isEqualTo(ReviewRequestType.REJECT);
                            ReviewRequest reviewRequest2 = reviewRequestReader.loadFrom(
                                    ReviewRequestId.of(request2Id)).get();
                            assertThat(reviewRequest2.getReviewRequestStatus()).isEqualTo(ReviewRequestType.REJECT);

                            ReviewRequest reviewRequest3 = reviewRequestReader.loadFrom(
                                    ReviewRequestId.of(request3Id)).get();
                            assertThat(reviewRequest3.getReviewRequestStatus()).isEqualTo(ReviewRequestType.MATCHED);
                            ReviewRequest reviewRequest4 = reviewRequestReader.loadFrom(
                                    ReviewRequestId.of(request4Id)).get();
                            assertThat(reviewRequest4.getReviewRequestStatus()).isEqualTo(ReviewRequestType.MATCHED);
                        }
                );
    }
}