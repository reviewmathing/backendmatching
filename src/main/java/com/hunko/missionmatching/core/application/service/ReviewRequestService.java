package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.Requester;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.domain.ReviewRequestSaver;
import com.hunko.missionmatching.core.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewRequestService {

    private final MissionValidator missionValidator;
    private final ReviewRequestSaver reviewRequestSaver;

    public Long request(Requester requester, MissionId missionId){
        if(!missionValidator.isInvalidMission(missionId)){
            ErrorType.INVALID_MISSION.throwException();
        }
        ReviewRequest request = new ReviewRequest(requester,missionId);
        try {
            return reviewRequestSaver.save(request);
        }catch (DataIntegrityViolationException e){
            throw ErrorType.DUPLICATE_REVIEW_REQUEST.toException();
        }
    }
}
