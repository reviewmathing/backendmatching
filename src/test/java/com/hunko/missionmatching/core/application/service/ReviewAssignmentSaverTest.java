package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import com.hunko.missionmatching.core.domain.Reviewee;
import com.hunko.missionmatching.core.domain.RevieweeId;
import com.hunko.missionmatching.core.domain.ReviewerId;
import com.hunko.missionmatching.core.domain.UserReader;
import com.hunko.missionmatching.core.scheduler.MissionSchedulerWalFactory;
import com.hunko.missionmatching.helper.StubFailMissionWalFactory;
import com.hunko.missionmatching.helper.TestGithubUri;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ReviewAssignmentSaverTest {

    @Autowired
    private ReviewAssignmentSaver reviewAssignmentSaver;

    @TestBean
    private MissionSchedulerWalFactory missionWalFactory;

    static MissionSchedulerWalFactory missionWalFactory() {
        return new StubFailMissionWalFactory();
    }

    @MockitoBean
    private UserReader userReader;

    @Test
    void save() {
        ArrayList<ReviewAssignment> saved = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            ReviewAssignment reviewAssignment = new ReviewAssignment(
                    null,
                    MissionId.of(1L),
                    ReviewerId.of((long) i),
                    List.of(
                            new Reviewee(RevieweeId.of(1L), TestGithubUri.GITHUB_URI),
                            new Reviewee(RevieweeId.of(2L), TestGithubUri.GITHUB_URI),
                            new Reviewee(RevieweeId.of(3L), TestGithubUri.GITHUB_URI),
                            new Reviewee(RevieweeId.of(4L), TestGithubUri.GITHUB_URI),
                            new Reviewee(RevieweeId.of(5L), TestGithubUri.GITHUB_URI)
                    ),
                    ZonedDateTime.now(),
                    ReviewAssignmentStatus.NOT_CLEARED
            );
            saved.add(reviewAssignment);
        }
        long before = System.currentTimeMillis();
        reviewAssignmentSaver.save(saved);
        long after = System.currentTimeMillis();
        System.out.println(after - before + "ms");
    }
}