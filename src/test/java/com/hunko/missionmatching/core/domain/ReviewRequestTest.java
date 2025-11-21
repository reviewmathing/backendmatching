package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hunko.missionmatching.core.exception.CoreException;
import org.junit.jupiter.api.Test;

class ReviewRequestTest {

    @Test
    void 리뷰신청철회() {
        ReviewRequest request = new ReviewRequest(Requester.of(1L), MissionId.of(1L), 3);

        request.cancel();

        assertThat(request.getReviewRequestStatus()).isEqualTo(ReviewRequestType.CANCEL);
    }

    @Test
    void 리뷰신청철회_실패() {
        ReviewRequest request = new ReviewRequest(null, Requester.of(1L), MissionId.of(1L), 3, null,
                ReviewRequestType.MATCHED);

        assertThatThrownBy(request::cancel).isInstanceOf(CoreException.class);
    }

    @Test
    void 깃허브uri업데이트() {
        ReviewRequest request = new ReviewRequest(Requester.of(1L), MissionId.of(1L), 3);
        GithubUri githubUri = TestGithubUrlFactory.createGithubUri();
        int reviewCount = 2;

        request.update(reviewCount,githubUri);

        assertThat(request.getGithubUri()).isEqualTo(githubUri);
        assertThat(request.getReviewCount()).isEqualTo(reviewCount);
    }

    @Test
    void 깃허브uri업데이트_실패() {
        ReviewRequest request = new ReviewRequest(null, Requester.of(1L), MissionId.of(1L), 3, null,
                ReviewRequestType.MATCHED);
        GithubUri githubUri = TestGithubUrlFactory.createGithubUri();

        assertThatThrownBy(() -> request.update(3, githubUri)).isInstanceOf(CoreException.class);
    }
}