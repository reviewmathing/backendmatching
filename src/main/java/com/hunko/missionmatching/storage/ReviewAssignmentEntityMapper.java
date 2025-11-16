package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.GithubUri;
import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.Reviewee;
import com.hunko.missionmatching.core.domain.RevieweeId;
import com.hunko.missionmatching.core.domain.ReviewerId;
import java.util.List;

public class ReviewAssignmentEntityMapper {
    private ReviewAssignmentEntityMapper() {
    }

    public static ReviewAssignmentEntity toEntity(ReviewAssignment reviewAssignment) {
        return new ReviewAssignmentEntity(
                reviewAssignment.getId(),
                reviewAssignment.getMissionId().toLong(),
                reviewAssignment.getReviewerId().toLong(),
                reviewAssignment.getLimitTime(),
                reviewAssignment.getReviewAssignmentStatus()
        );
    }

    public static ReviewAssignment toReviewAssignment(ReviewAssignmentEntity reviewAssignment,
                                                      List<ReviewAssignmentRevieweeEntity> reviewees) {
        List<Reviewee> reviewee = toReviewee(reviewees);
        return new ReviewAssignment(
                reviewAssignment.getId(),
                MissionId.of(reviewAssignment.getMissionId()),
                ReviewerId.of(reviewAssignment.getReviewerId()),
                reviewee,
                reviewAssignment.getLimitTime(),
                reviewAssignment.getReviewAssignmentStatus()
        );
    }

    public static ReviewAssignment toReviewAssignment(ReviewAssignmentRevieweeEntity reviewAssignmentRevieweeEntity) {
        ReviewAssignmentEntity reviewAssignmentEntity = reviewAssignmentRevieweeEntity.getReviewAssignmentEntity();
        return toReviewAssignment(reviewAssignmentEntity, List.of(reviewAssignmentRevieweeEntity));
    }

    private static List<Reviewee> toReviewee(List<ReviewAssignmentRevieweeEntity> reviewees) {
        return reviewees.stream().map(r ->
                new Reviewee(
                        r.getId(),
                        RevieweeId.of(r.getRevieweeId()),
                        GithubUri.of(r.getGitHubUri()),
                        r.getReviewStatus()
                )
        ).toList();
    }
}
