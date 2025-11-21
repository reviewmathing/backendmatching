package com.hunko.missionmatching.core.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import com.hunko.missionmatching.core.domain.ReviewStatus;
import com.hunko.missionmatching.core.domain.Reviewee;
import com.hunko.missionmatching.core.domain.RevieweeId;
import com.hunko.missionmatching.core.domain.ReviewerId;
import com.hunko.missionmatching.core.exception.CoreException;
import com.hunko.missionmatching.helper.EmptyReviewAssigmentReader;
import com.hunko.missionmatching.helper.FakeLocalDateFactory;
import com.hunko.missionmatching.helper.SingleReviewAssigmentReader;
import com.hunko.missionmatching.helper.TestGithubUri;
import com.hunko.missionmatching.util.TestServerTimeSetup;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReviewAssigmentServiceTest implements TestServerTimeSetup {

    @Test
    void 다른사용자_assigment조회시_예외() {
        ReviewAssigmentService reviewAssigmentService = new ReviewAssigmentService(
                new SingleReviewAssigmentReader(createReviewAssignment(1)),
                null);

        assertThatThrownBy(() -> reviewAssigmentService.loadFrom(2L, 1L)).isInstanceOf(CoreException.class);
    }

    @Test
    void 같은사용자이면_정상처리() {
        ReviewAssigmentService reviewAssigmentService = new ReviewAssigmentService(
                new SingleReviewAssigmentReader(createReviewAssignment(1)),
                null);
        assertThatCode(() -> reviewAssigmentService.loadFrom(1L, 1L)).doesNotThrowAnyException();
    }

    @Test
    void 리뷰완료처리_성공() {
        setUpServerTimeFactory(new FakeLocalDateFactory());
        FakeAssignmentSaver fakeAssignmentSaver = new FakeAssignmentSaver();
        ReviewAssigmentService reviewAssigmentService = new ReviewAssigmentService(
                new SingleReviewAssigmentReader(createReviewAssignment(1)),
                fakeAssignmentSaver);

        reviewAssigmentService.complete(1L, ReviewerId.of(1L), 2L);

        ReviewAssignment saved = fakeAssignmentSaver.getSaved();
        assertThat(saved.getReviewAssignmentStatus()).isEqualTo(ReviewAssignmentStatus.ALL_CLEARED);
    }

    @Test
    void 그룹검색실패() {
        setUpServerTimeFactory(new FakeLocalDateFactory());
        FakeAssignmentSaver fakeAssignmentSaver = new FakeAssignmentSaver();
        ReviewAssigmentService reviewAssigmentService = new ReviewAssigmentService(new EmptyReviewAssigmentReader(),
                fakeAssignmentSaver);

        assertThatThrownBy(() -> reviewAssigmentService.complete(1L, ReviewerId.of(1L), 2L)).isInstanceOf(
                CoreException.class);
    }

    @Test
    void 다른대상그룹_완료처리() {
        setUpServerTimeFactory(new FakeLocalDateFactory());
        FakeAssignmentSaver fakeAssignmentSaver = new FakeAssignmentSaver();
        ReviewAssigmentService reviewAssigmentService = new ReviewAssigmentService(
                new SingleReviewAssigmentReader(createReviewAssignment(1)),
                fakeAssignmentSaver);

        assertThatThrownBy(() -> reviewAssigmentService.complete(1L, ReviewerId.of(2L), 2L)).isInstanceOf(
                CoreException.class);
    }

    private ReviewAssignment createReviewAssignment(long id) {
        return new ReviewAssignment(
                id,
                MissionId.of(1L),
                ReviewerId.of(1L),
                List.of(
                        new Reviewee(
                                2L,
                                RevieweeId.of(2L),
                                TestGithubUri.GITHUB_URI,
                                ReviewStatus.PENDING
                        )
                ),
                ZonedDateTime.now(),
                ReviewAssignmentStatus.NOT_CLEARED
        );
    }

    private static class FakeAssignmentSaver extends ReviewAssignmentSaver {

        private ReviewAssignment reviewAssignment;

        public FakeAssignmentSaver() {
            super(null, null);
        }

        @Override
        public void save(ReviewAssignment reviewAssignment) {
            this.reviewAssignment = reviewAssignment;
        }

        public ReviewAssignment getSaved() {
            return reviewAssignment;
        }
    }
}