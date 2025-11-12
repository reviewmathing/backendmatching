package com.hunko.missionmatching.core.domain;

import lombok.Getter;

@Getter
public class ReviewRequest {

    private final ReviewRequestId reviewRequestId;
    private final Requester requester;
    private final MissionId missionId;

    public ReviewRequest(ReviewRequestId reviewRequestId, Requester requester, MissionId missionId) {
        this.reviewRequestId = reviewRequestId;
        this.requester = requester;
        this.missionId = missionId;
    }

    public ReviewRequest(Requester requester, MissionId missionId) {
        this(ReviewRequestId.empty(), requester, missionId);
    }
}
