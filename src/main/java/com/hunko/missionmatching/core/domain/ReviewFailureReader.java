package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.storage.ReviewFailureEntity;
import com.hunko.missionmatching.storage.ReviewFailureRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewFailureReader {

    private final ReviewFailureRepository reviewFailureRepository;

    public List<ReviewFailure> readFrom(ReviewerId reviewerId) {
        List<ReviewFailureEntity> reviewFailureEntities = reviewFailureRepository.findAllByReviewerId(reviewerId.toLong());
        return reviewFailureEntities.stream().map(this::toReviewFailure).toList();
    }

    private ReviewFailure toReviewFailure(ReviewFailureEntity reviewFailureEntity) {
        return new ReviewFailure(
                MissionId.of(reviewFailureEntity.getMissionId()),
                ReviewerId.of(reviewFailureEntity.getReviewerId())
        );
    }
}
