package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.ReviewRequest;

public class ReviewRequestEntityMapper {
    private ReviewRequestEntityMapper() {
    }
    public static ReviewRequestEntity toEntity(ReviewRequest reviewRequest) {
        return new ReviewRequestEntity(
                reviewRequest.getReviewRequestId().id(),
                reviewRequest.getRequester().id(),
                reviewRequest.getMissionId().toLong());
    }
}
