package com.hunko.missionmatching.helper;

import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentReader;
import java.util.Optional;

public class SingleReviewAssigmentReader extends ReviewAssignmentReader {

    private final ReviewAssignment reviewAssignment;

    public SingleReviewAssigmentReader(ReviewAssignment reviewAssignment) {
        super(null, null);
        this.reviewAssignment = reviewAssignment;
    }

    @Override
    public Optional<ReviewAssignment> loadFrom(Long id) {
        return Optional.ofNullable(reviewAssignment);
    }
}
