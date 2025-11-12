package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.storage.ReviewRequestEntity;
import com.hunko.missionmatching.storage.ReviewRequestEntityMapper;
import com.hunko.missionmatching.storage.ReviewRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReviewRequestSaver {

    private final ReviewRequestRepository reviewRequestRepository;

    @Transactional
    public Long save(ReviewRequest request) {
        ReviewRequestEntity reviewRequestEntity = ReviewRequestEntityMapper.toEntity(request);
        ReviewRequestEntity save = reviewRequestRepository.save(reviewRequestEntity);
        return save.getId();
    }
}
