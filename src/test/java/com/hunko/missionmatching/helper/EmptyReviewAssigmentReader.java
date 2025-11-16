package com.hunko.missionmatching.helper;

import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentReader;
import java.util.Optional;

public class EmptyReviewAssigmentReader extends ReviewAssignmentReader {


    public EmptyReviewAssigmentReader() {
        super(null, null);
    }

    @Override
    public Optional<ReviewAssignment> loadFrom(Long id) {
        return Optional.empty();
    }
}
