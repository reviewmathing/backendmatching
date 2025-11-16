package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hunko.missionmatching.core.exception.CoreException;
import com.hunko.missionmatching.helper.TestGithubUri;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReviewAssignmentTest {

    @Test
    void 리뷰어_업데이트() {
        ReviewAssignment reviewAssignment = new ReviewAssignment(
                MissionId.of(1L),
                ReviewerId.of(1L),
                ZonedDateTime.now().plusDays(1),
                List.of(
                        new Reviewee(
                                1L,
                                RevieweeId.of(2L),
                                TestGithubUri.GITHUB_URI,
                                ReviewStatus.PENDING
                        ),
                        new Reviewee(
                                2L,
                                RevieweeId.of(3L),
                                TestGithubUri.GITHUB_URI,
                                ReviewStatus.PENDING
                        )
                )
        );

        reviewAssignment.completeReview(LocalDateTime.now(), 1L);

        Reviewee reviewee1 = reviewAssignment.getReviewees().stream().filter(reviewee -> reviewee.getId().equals(1L))
                .findAny().get();
        assertThat(reviewee1.getReviewStatus()).isEqualTo(ReviewStatus.COMPLETED);
        Reviewee reviewee2 = reviewAssignment.getReviewees().stream().filter(reviewee -> reviewee.getId().equals(2L))
                .findAny().get();
        assertThat(reviewee2.getReviewStatus()).isEqualTo(ReviewStatus.PENDING);
    }

    @Test
    void 전체가_리뷰를_성공하면_전체완료상태로변경() {
        ReviewAssignment reviewAssignment = new ReviewAssignment(
                MissionId.of(1L),
                ReviewerId.of(1L),
                ZonedDateTime.now().plusDays(1),
                List.of(
                        new Reviewee(
                                1L,
                                RevieweeId.of(2L),
                                TestGithubUri.GITHUB_URI,
                                ReviewStatus.COMPLETED
                        ),
                        new Reviewee(
                                2L,
                                RevieweeId.of(3L),
                                TestGithubUri.GITHUB_URI,
                                ReviewStatus.PENDING
                        )
                )
        );

        reviewAssignment.completeReview(LocalDateTime.now(), 2L);

        assertThat(reviewAssignment.getReviewAssignmentStatus()).isEqualTo(ReviewAssignmentStatus.ALL_CLEARED);
    }

    @Test
    void 시간이_지나면_업데이트_실패() {
        ReviewAssignment reviewAssignment = new ReviewAssignment(
                MissionId.of(1L),
                ReviewerId.of(1L),
                ZonedDateTime.now(),
                List.of(
                        new Reviewee(
                                1L,
                                RevieweeId.of(2L),
                                TestGithubUri.GITHUB_URI,
                                ReviewStatus.PENDING
                        ),
                        new Reviewee(
                                2L,
                                RevieweeId.of(3L),
                                TestGithubUri.GITHUB_URI,
                                ReviewStatus.PENDING
                        )
                )
        );

        assertThatThrownBy(() -> reviewAssignment.completeReview(LocalDateTime.now(), 1L)).isInstanceOf(CoreException.class);
    }

}