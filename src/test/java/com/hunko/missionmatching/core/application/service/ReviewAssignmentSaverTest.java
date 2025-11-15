package com.hunko.missionmatching.core.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import com.hunko.missionmatching.core.domain.Reviewee;
import com.hunko.missionmatching.core.domain.RevieweeId;
import com.hunko.missionmatching.core.domain.ReviewerId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReviewAssignmentSaverTest {

    @Autowired
    private ReviewAssignmentSaver reviewAssignmentSaver;

    @Test
    void save() {
        ArrayList<ReviewAssignment> saved = new ArrayList<>();
        for(int i = 1; i <= 100; i++){
            ReviewAssignment reviewAssignment = new ReviewAssignment(
                    null,
                    MissionId.of(1L),
                    ReviewerId.of((long) i),
                    List.of(
                            new Reviewee(RevieweeId.of(1L)),
                            new Reviewee(RevieweeId.of(2L)),
                            new Reviewee(RevieweeId.of(3L)),
                            new Reviewee(RevieweeId.of(4L)),
                            new Reviewee(RevieweeId.of(5L))
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