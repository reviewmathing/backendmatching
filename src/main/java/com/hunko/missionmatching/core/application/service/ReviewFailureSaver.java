package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.ReviewFailure;
import com.hunko.missionmatching.storage.ReviewFailureEntity;
import com.hunko.missionmatching.storage.ReviewFailureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReviewFailureSaver {

    private final ReviewFailureRepository reviewFailureRepository;

    @Transactional
    public void save(ReviewFailure reviewFailure) {
        reviewFailureRepository.save(
                new ReviewFailureEntity(
                        reviewFailure.missionId().toLong(),
                        reviewFailure.reviewerId().toLong())
        );
    }
}
