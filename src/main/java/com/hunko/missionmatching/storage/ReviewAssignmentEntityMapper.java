package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.ReviewAssignment;

public class ReviewAssignmentEntityMapper {
    private ReviewAssignmentEntityMapper() {
    }

    public static ReviewAssignmentEntity toEntity(ReviewAssignment reviewAssignment) {
        return new ReviewAssignmentEntity(
                reviewAssignment.getId(),
                reviewAssignment.getMissionId().toLong(),
                reviewAssignment.getReviewerId().toLong()
        );
    }
}
