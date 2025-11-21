package com.hunko.missionmatching.core.application.event;

import com.hunko.missionmatching.core.application.service.ReviewAssignmentSaver;
import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import com.hunko.missionmatching.core.domain.ReviewerId;
import com.hunko.missionmatching.core.domain.UserReader;
import com.hunko.missionmatching.core.scheduler.MissionSchedulerWalFactory;
import com.hunko.missionmatching.helper.StubFailMissionWalFactory;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ReviewAssignmentListenerTest {

    @Autowired
    private ReviewAssignmentSaver reviewAssignmentSaver;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @TestBean
    private MissionSchedulerWalFactory missionWalFactory;

    static MissionSchedulerWalFactory missionWalFactory() {
        return new StubFailMissionWalFactory();
    }

    @MockitoBean
    private UserReader userReader;

    @Test
    void 리뷰패널티생성() {
        reviewAssignmentSaver.save(new ReviewAssignment(
                null,
                MissionId.of(1L),
                ReviewerId.of(1L),
                List.of(),
                ZonedDateTime.now(),
                ReviewAssignmentStatus.TIME_OUT
        ));

    }
}