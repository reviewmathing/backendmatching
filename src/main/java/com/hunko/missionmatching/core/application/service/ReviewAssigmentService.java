package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewAssigmentService {

    private final ReviewAssignmentReader reviewAssignmentReader;

    public Page<ReviewAssignment> loadFrom(Long userId, Integer page, int limit) {
        return reviewAssignmentReader.loadFrom(userId, page, limit);
    }
}
