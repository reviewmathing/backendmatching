package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.GithubUri;
import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.Requester;
import com.hunko.missionmatching.core.domain.ReviewGithubUrlUpdateService;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.domain.ReviewRequestId;
import com.hunko.missionmatching.core.domain.ReviewRequestSaver;
import com.hunko.missionmatching.core.domain.UserValidator;
import com.hunko.missionmatching.core.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewRequestService {

    private final UserValidator userValidator;
    private final MissionValidator missionValidator;
    private final ReviewRequestSaver reviewRequestSaver;
    private final ReviewRequestReader reviewRequestReader;
    private final ReviewGithubUrlUpdateService reviewGithubUrlUpdateService;

    public Long request(Requester requester, MissionId missionId, Integer reviewCount) {
        if (!missionValidator.isInvalidMission(missionId)) {
            ErrorType.INVALID_MISSION.throwException();
        }
        if (!userValidator.canRequestReview(requester.toLong())){
            //todo : 추후 수정예정
            ErrorType.INVALID_INPUT.throwException();
        }
        ReviewRequest request = new ReviewRequest(requester, missionId, reviewCount);
        try {
            return reviewRequestSaver.save(request);
        } catch (DataIntegrityViolationException e) {
            throw ErrorType.DUPLICATE_REVIEW_REQUEST.toException();
        }
    }

    public void updateGithubUri(ReviewRequestId reviewRequestId, MissionId missionId, Requester requester,
                                GithubUri githubUri) {
        ReviewRequest reviewRequest = getReviewRequest(reviewRequestId, missionId,
                requester);
        ReviewRequest request = reviewGithubUrlUpdateService.updateGithubUrl(reviewRequest, githubUri);
        reviewRequestSaver.save(request);
    }

    public void cancel(ReviewRequestId reviewRequestId, MissionId missionId, Requester requester) {
        ReviewRequest reviewRequest = getReviewRequest(reviewRequestId, missionId, requester);
        reviewRequest.cancel();
        reviewRequestSaver.save(reviewRequest);
    }

    private ReviewRequest getReviewRequest(ReviewRequestId reviewRequestId, MissionId missionId, Requester requester) {
        ReviewRequest reviewRequest = reviewRequestReader.loadFrom(requester, reviewRequestId)
                .orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);
        if (!reviewRequest.getMissionId().equals(missionId)) {
            ErrorType.INVALID_INPUT.throwException();
        }
        return reviewRequest;
    }

    @Transactional
    public void matched(ReviewRequestId reviewRequestId) {
        ReviewRequest request = reviewRequestReader.loadFrom(reviewRequestId)
                .orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);
        request.matched();
        reviewRequestSaver.save(request);
    }
}
