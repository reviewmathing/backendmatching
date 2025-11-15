package com.hunko.missionmatching.core.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentReader;
import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import com.hunko.missionmatching.core.domain.ReviewerId;
import com.hunko.missionmatching.core.exception.CoreException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ReviewAssigmentServiceTest {

    @Test
    void 다른사용자_assigment조회시_예외() {
        ReviewAssigmentService reviewAssigmentService = new ReviewAssigmentService(new FakeReviewAssigmentReader());

        assertThatThrownBy(()->reviewAssigmentService.loadFrom(2L,1L)).isInstanceOf(CoreException.class);
    }

    @Test
    void 같은사용자이면_정상처리() {
        ReviewAssigmentService reviewAssigmentService = new ReviewAssigmentService(new FakeReviewAssigmentReader());
        assertThatCode(() -> reviewAssigmentService.loadFrom(1L,1L)).doesNotThrowAnyException();
    }

    private static class FakeReviewAssigmentReader extends ReviewAssignmentReader {

        public FakeReviewAssigmentReader() {
            super(null, null);
        }

        @Override
        public Optional<ReviewAssignment> loadFrom(Long id) {
            return Optional.of(
                    new ReviewAssignment(
                            id,
                            MissionId.of(1L),
                            ReviewerId.of(1L),
                            List.of(),
                            ZonedDateTime.now(),
                            ReviewAssignmentStatus.NOT_CLEARED
                    )
            );
        }
    }
}