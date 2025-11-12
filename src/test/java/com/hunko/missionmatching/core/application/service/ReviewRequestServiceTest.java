package com.hunko.missionmatching.core.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.Requester;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.domain.ReviewRequestId;
import com.hunko.missionmatching.core.exception.CoreException;
import com.hunko.missionmatching.helper.FakeReviewRequestSaver;
import com.hunko.missionmatching.helper.RuntimeExceptionThrowReviewRequestSaver;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

class ReviewRequestServiceTest {

    @Test
    void 정상생성() {
        FakeReviewRequestSaver fakeReviewRequestSaver = new FakeReviewRequestSaver();
        ReviewRequestService reviewRequestService = new ReviewRequestService(new FakeMissionValidator(true),
                fakeReviewRequestSaver);

        Long id = reviewRequestService.request(new Requester(1L), MissionId.of(1L));

        ReviewRequest request = fakeReviewRequestSaver.getRequest(new ReviewRequestId(id));
        assertThat(request.getRequester()).isEqualTo(new Requester(1L));
        assertThat(request.getMissionId()).isEqualTo(MissionId.of(1L));
    }

    @Test
    void 존재하지_않는_미션으로_요청() {
        ReviewRequestService reviewRequestService = new ReviewRequestService(new FakeMissionValidator(false),
                new FakeReviewRequestSaver());

        assertThatThrownBy(() -> reviewRequestService.request(new Requester(1L), MissionId.of(1L)))
                .isInstanceOf(CoreException.class);
    }

    @Test
    void 중복저장() {
        RuntimeExceptionThrowReviewRequestSaver reviewRequestSaver = new RuntimeExceptionThrowReviewRequestSaver(
                new DataIntegrityViolationException("test"));
        ReviewRequestService reviewRequestService = new ReviewRequestService(new FakeMissionValidator(false),
                reviewRequestSaver);

        assertThatThrownBy(() -> reviewRequestService.request(new Requester(1L), MissionId.of(1L)))
                .isInstanceOf(CoreException.class);
    }

    private static class FakeMissionValidator extends MissionValidator {

        private final boolean valid;

        public FakeMissionValidator(boolean valid) {
            super(null);
            this.valid = valid;
        }

        @Override
        public boolean isInvalidMission(MissionId missionId) {
            return valid;
        }
    }
}