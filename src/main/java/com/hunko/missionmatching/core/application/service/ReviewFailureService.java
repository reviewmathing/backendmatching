package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentReader;
import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import com.hunko.missionmatching.core.domain.ReviewFailure;
import com.hunko.missionmatching.core.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewFailureService {

    private final ReviewAssignmentReader reader;
    private final ReviewFailureSaver reviewFailureSaver;

    public void createFailerReview(Long reviewAssignmentId){
        ReviewAssignment reviewAssignment = reader.loadFrom(reviewAssignmentId).get();
        if(!reviewAssignment.getReviewAssignmentStatus().equals(ReviewAssignmentStatus.TIME_OUT)){
            //todo: 추후 예외처리예정
            ErrorType.INVALID_REVIEW_ASSIGNMENT_STATE.throwException();
        }
        reviewFailureSaver.save(new ReviewFailure(reviewAssignment.getMissionId(),reviewAssignment.getReviewerId()));
    }
}
