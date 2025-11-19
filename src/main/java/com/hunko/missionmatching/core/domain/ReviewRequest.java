package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.core.exception.ErrorType;
import lombok.Getter;

@Getter
public class ReviewRequest {

    private final ReviewRequestId reviewRequestId;
    private final Requester requester;
    private final MissionId missionId;
    private final Integer reviewCount;
    private GithubUri githubUri;
    private ReviewRequestType reviewRequestStatus;

    public ReviewRequest(ReviewRequestId reviewRequestId, Requester requester, MissionId missionId, Integer reviewCount,
                         GithubUri githubUri, ReviewRequestType reviewRequestStatus) {
        this.reviewRequestId = reviewRequestId;
        this.requester = requester;
        this.missionId = missionId;
        this.reviewCount = reviewCount;
        this.githubUri = githubUri;
        this.reviewRequestStatus = reviewRequestStatus;
    }

    public ReviewRequest(Requester requester, MissionId missionId, Integer reviewCount) {
        this(ReviewRequestId.empty(), requester, missionId, reviewCount, null, ReviewRequestType.REQUEST);
    }

    public void updateGithubUrl(GithubUri githubUri) {
        if (!ReviewRequestType.REQUEST.equals(reviewRequestStatus)) {
            ErrorType.INVALID_REVIEW_REQUEST_STATE.throwException();
        }
        this.githubUri = githubUri;
    }

    public void cancel() {
        if (!ReviewRequestType.REQUEST.equals(this.reviewRequestStatus)) {
            ErrorType.INVALID_REVIEW_REQUEST_STATE.throwException();
        }
        this.reviewRequestStatus = ReviewRequestType.CANCEL;
    }

    public void reject() {
        this.reviewRequestStatus = ReviewRequestType.REJECT;
    }

    public void matched() {
        if (!ReviewRequestType.REQUEST.equals(this.reviewRequestStatus)) {
            ErrorType.INVALID_REVIEW_REQUEST_STATE.throwException();
        }
        this.reviewRequestStatus = ReviewRequestType.MATCHED;
    }
}
