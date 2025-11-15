package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.Requester;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.domain.ReviewRequestId;
import com.hunko.missionmatching.storage.ReviewRequestEntity;
import com.hunko.missionmatching.storage.ReviewRequestEntityMapper;
import com.hunko.missionmatching.storage.ReviewRequestRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewRequestReader {

    private final ReviewRequestRepository reviewRequestRepository;

    public Optional<ReviewRequest> loadFrom(Requester requester, ReviewRequestId requestId) {
        Optional<ReviewRequestEntity> requestEntity = reviewRequestRepository.findByRequesterIdAndId(
                requester.toLong(), requestId.toLong());
        return requestEntity.map(ReviewRequestEntityMapper::toReviewRequest);
    }

    public Optional<ReviewRequest> loadFrom(ReviewRequestId requestId) {
        Optional<ReviewRequestEntity> requestEntity = reviewRequestRepository.findById(requestId.toLong());
        return requestEntity.map(ReviewRequestEntityMapper::toReviewRequest);
    }
}
