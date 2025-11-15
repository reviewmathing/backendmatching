package com.hunko.missionmatching.core.domain;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

@Getter
public class ReviewAssignment {

    private final Long id;
    private final MissionId missionId;
    private final ReviewerId reviewerId;
    private final List<Reviewee> reviewee;
    private final ZonedDateTime limitTime;
    private ReviewAssignmentStatus reviewAssignmentStatus;

    public ReviewAssignment(Long id, MissionId missionId, ReviewerId reviewerId, List<Reviewee> reviewee,
                            ZonedDateTime limitTime, ReviewAssignmentStatus reviewAssignmentStatus) {
        this.id = id;
        this.missionId = missionId;
        this.reviewerId = reviewerId;
        this.reviewee = reviewee;
        this.limitTime = limitTime;
        this.reviewAssignmentStatus = reviewAssignmentStatus;
    }

    public ReviewAssignment(MissionId missionId, ReviewerId reviewerId, ZonedDateTime limitTime,
                            List<Reviewee> reviewee) {
        this(null, missionId, reviewerId, reviewee, limitTime, ReviewAssignmentStatus.NOT_CLEARED);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewAssignment that = (ReviewAssignment) o;
        return Objects.equals(missionId, that.missionId) && Objects.equals(reviewerId, that.reviewerId)
                && Objects.equals(reviewee, that.reviewee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(missionId, reviewerId, reviewee);
    }
}
