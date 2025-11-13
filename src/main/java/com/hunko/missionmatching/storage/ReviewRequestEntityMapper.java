package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.core.domain.GithubUri;
import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.Requester;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.domain.ReviewRequestId;

public class ReviewRequestEntityMapper {
    private ReviewRequestEntityMapper() {
    }
    public static ReviewRequestEntity toEntity(ReviewRequest reviewRequest) {
        return new ReviewRequestEntity(
                reviewRequest.getReviewRequestId().id(),
                reviewRequest.getMissionId().toLong(),
                reviewRequest.getRequester().id(),
                reviewRequest.getReviewCount(),
                reviewRequest.getGithubUri() != null ? reviewRequest.getGithubUri().toUriString() : null
        );
    }

    public static ReviewRequest toReviewRequest(ReviewRequestEntity reviewRequestEntity) {
        return new ReviewRequest(
                ReviewRequestId.of(reviewRequestEntity.getId()),
                Requester.of(reviewRequestEntity.getRequesterId()),
                MissionId.of(reviewRequestEntity.getMissionId()),
                reviewRequestEntity.getReviewCount(),
                reviewRequestEntity.getGithubUrl() == null ? null : GithubUri.of(reviewRequestEntity.getGithubUrl())
        );
    }
}
