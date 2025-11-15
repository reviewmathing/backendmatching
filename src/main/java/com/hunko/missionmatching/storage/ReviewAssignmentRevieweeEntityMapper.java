package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.Reviewee;
import java.util.List;

public class ReviewAssignmentRevieweeEntityMapper {

    public static List<ReviewAssignmentRevieweeEntity> toEntities(ReviewAssignmentEntity entity,
                                                                  List<Reviewee> reviewee) {
        return reviewee.stream().map(r -> toEntity(entity, r)).toList();
    }

    private static ReviewAssignmentRevieweeEntity toEntity(ReviewAssignmentEntity reviewAssignment, Reviewee reviewee) {
        return new ReviewAssignmentRevieweeEntity(
                reviewee.getId(),
                reviewAssignment,
                reviewee.getRevieweeId().toLong(),
                reviewee.getGithubUri().toUriString(),
                reviewee.getReviewStatus()
        );
    }
}
