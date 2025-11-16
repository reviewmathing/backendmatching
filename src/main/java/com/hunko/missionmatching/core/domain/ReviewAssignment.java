package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.core.exception.ErrorType;
import com.hunko.missionmatching.util.DateUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

@Getter
public class ReviewAssignment {

    private final Long id;
    private final MissionId missionId;
    private final ReviewerId reviewerId;
    private final List<Reviewee> reviewees;
    private final ZonedDateTime limitTime;
    private ReviewAssignmentStatus reviewAssignmentStatus;

    public ReviewAssignment(Long id, MissionId missionId, ReviewerId reviewerId, List<Reviewee> reviewees,
                            ZonedDateTime limitTime, ReviewAssignmentStatus reviewAssignmentStatus) {
        this.id = id;
        this.missionId = missionId;
        this.reviewerId = reviewerId;
        this.reviewees = reviewees;
        this.limitTime = limitTime;
        this.reviewAssignmentStatus = reviewAssignmentStatus;
    }

    public ReviewAssignment(MissionId missionId, ReviewerId reviewerId, ZonedDateTime limitTime,
                            List<Reviewee> reviewees) {
        this(null, missionId, reviewerId, reviewees, limitTime, ReviewAssignmentStatus.NOT_CLEARED);
    }


    public void completeReview(LocalDateTime now, Long revieweeId) {
        LocalDateTime serverDateTime = DateUtil.toServerDateTime(limitTime);
        if(serverDateTime.isBefore(now)){
            //todo: 예외 변경예정
            ErrorType.INVALID_INPUT.throwException();
        }

        Reviewee reviewee = reviewees.stream().filter(r -> r.getId().equals(revieweeId)).findFirst()
                .orElseThrow(ErrorType.ENTITY_NOT_FOUND::toException);
        reviewee.complete();

        if(ReviewAssignmentStatus.NOT_CLEARED.equals(this.reviewAssignmentStatus) && isAllComplete()){
            this.reviewAssignmentStatus = ReviewAssignmentStatus.ALL_CLEARED;
        }
    }

    public boolean isAllComplete() {
        for (Reviewee reviewee : reviewees) {
            if(reviewee.getReviewStatus().equals(ReviewStatus.PENDING)){
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewAssignment that = (ReviewAssignment) o;
        return Objects.equals(missionId, that.missionId) && Objects.equals(reviewerId, that.reviewerId)
                && Objects.equals(reviewees, that.reviewees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(missionId, reviewerId, reviewees);
    }
}
