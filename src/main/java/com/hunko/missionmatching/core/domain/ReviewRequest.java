package com.hunko.missionmatching.core.domain;

import lombok.Getter;

@Getter
public class ReviewRequest {

    private final ReviewRequestId reviewRequestId;
    private final Requester requester;
    private final MissionId missionId;
    private final Integer reviewCount;
    private GithubUri githubUri;

    public ReviewRequest(ReviewRequestId reviewRequestId, Requester requester, MissionId missionId, Integer reviewCount, GithubUri githubUri) {
        this.reviewRequestId = reviewRequestId;
        this.requester = requester;
        this.missionId = missionId;
        this.reviewCount = reviewCount;
        this.githubUri = githubUri;
    }

    public ReviewRequest(Requester requester, MissionId missionId, Integer reviewCount) {
        this(ReviewRequestId.empty(), requester, missionId,reviewCount, null);
    }

    public void updateGithubUrl(GithubUri githubUri) {
        this.githubUri = githubUri;
    }
}
