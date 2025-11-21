package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.core.exception.ErrorType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRequestUpdateService {

    private final MissionReader missionReader;

    public ReviewRequest updateGithubUrl(ReviewRequest request, int reviewCount, GithubUri githubUri) {
        MissionId missionId = request.getMissionId();
        Mission mission = missionReader.readById(missionId.toLong())
                .orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);

        if (!MissionStatus.ONGOING.equals(mission.getStatus())) {
            ErrorType.INVALID_MISSION_STATE.throwException();
        }
        GithubUri missionUrl = mission.getMissionUrl();
        if (!missionUrl.isSubUrl(githubUri)) {
            ErrorType.NOT_SUB_URL_FOR_MISSION.throwException();
        }
        request.update(reviewCount, githubUri);
        return request;
    }

}
