package com.hunko.missionmatching.core.application.event;

import static org.junit.jupiter.api.Assertions.*;

import com.hunko.missionmatching.core.application.service.ReviewAssignmentSaver;
import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import com.hunko.missionmatching.core.domain.ReviewTimeOut;
import com.hunko.missionmatching.core.domain.ReviewerId;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest
class ReviewAssignmentListenerTest {

    @Autowired
    private ReviewAssignmentSaver reviewAssignmentSaver;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

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