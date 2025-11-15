package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hunko.missionmatching.core.application.service.ReviewAssignmentSaver;
import com.hunko.missionmatching.storage.ReviewAssignmentRepository;
import com.hunko.missionmatching.storage.ReviewAssignmentRevieweeRepository;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;

@DataJpaTest
class ReviewAssignmentStoreTest {

    @Autowired
    private ReviewAssignmentRepository reviewAssigmentRepository;
    @Autowired
    private ReviewAssignmentRevieweeRepository reviewAssigmentRevieweeRepository;

    @Test
    public void id가느린순으로데이터를_가지고온다() {
        ReviewAssignmentSaver reviewAssignmentSaver = new ReviewAssignmentSaver(reviewAssigmentRepository,
                reviewAssigmentRevieweeRepository);
        initReviewAssigment(reviewAssignmentSaver,10);

        ReviewAssignmentReader reader = new ReviewAssignmentReader(reviewAssigmentRepository,
                reviewAssigmentRevieweeRepository);
        Page<ReviewAssignment> reviewAssignments = reader.loadFrom(1L, 0, 9);

        assertThat(reviewAssignments).hasSize(9);
        assertThat(reviewAssignments.stream().map(ReviewAssignment::getId)).doesNotContain(1L);
    }

    private static void initReviewAssigment(ReviewAssignmentSaver reviewAssignmentSaver,int missionCount) {
        ArrayList<ReviewAssignment> assignments = new ArrayList<>();
        for (int i = 0; i < missionCount; i++) {
            ReviewAssignment reviewAssignment = new ReviewAssignment(
                    MissionId.of((long) i),
                    ReviewerId.of(1L),
                    ZonedDateTime.now(),
                    List.of(
                            new Reviewee(
                                    RevieweeId.of(2L)
                            )
                    )
            );
            assignments.add(reviewAssignment);

        }
        reviewAssignmentSaver.save(assignments);
    }
}