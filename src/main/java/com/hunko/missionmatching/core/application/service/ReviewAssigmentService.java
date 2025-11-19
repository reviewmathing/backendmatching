package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.MissionId;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentReader;
import com.hunko.missionmatching.core.domain.RevieweeId;
import com.hunko.missionmatching.core.domain.ReviewerId;
import com.hunko.missionmatching.core.exception.ErrorType;
import com.hunko.missionmatching.util.ServerTime;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewAssigmentService {

    private final ReviewAssignmentReader reviewAssignmentReader;
    private final ReviewAssignmentSaver reviewAssignmentSaver;

    public Page<ReviewAssignment> loadFrom(Long userId, Integer page, int limit) {
        return reviewAssignmentReader.loadFrom(userId, page, limit);
    }

    public ReviewAssignment loadFrom(Long userId, Long reviewassigmentId) {
        ReviewAssignment reviewAssignment = reviewAssignmentReader.loadFrom(reviewassigmentId)
                .orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);
        if (!reviewAssignment.getReviewerId().equals(ReviewerId.of(userId))) {
            ErrorType.UNAUTHORIZED_ACTION.throwException();
        }
        return reviewAssignment;
    }

    public List<ReviewAssignment> loadAssignmentsFrom(MissionId missionId, RevieweeId revieweeId) {
        return reviewAssignmentReader.loadAssignmentsFrom(missionId,revieweeId);
    }

    public void complete(Long assigmentId, ReviewerId reviewerId, Long revieweeId) {
        ReviewAssignment reviewAssignment = reviewAssignmentReader.loadFrom(assigmentId)
                .orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);
        if (!reviewAssignment.getReviewerId().equals(reviewerId)) {
            ErrorType.UNAUTHORIZED_ACTION.throwException();
        }
        LocalDateTime now = ServerTime.now();
        reviewAssignment.completeReview(now, revieweeId);
        reviewAssignmentSaver.save(reviewAssignment);
    }
}
