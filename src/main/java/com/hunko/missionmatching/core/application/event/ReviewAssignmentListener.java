package com.hunko.missionmatching.core.application.event;

import com.hunko.missionmatching.core.application.service.ReviewFailureService;
import com.hunko.missionmatching.core.domain.ReviewTimeOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewAssignmentListener {

    private final ReviewFailureService reviewFailureService;

    @Async("threadPoolTaskExecutor")
    @TransactionalEventListener(value = ReviewTimeOut.class)
    public void handleReviewAssignmentEvent(ReviewTimeOut reviewAssignment) {
        try {
            reviewFailureService.createFailerReview(reviewAssignment.assignmentId());
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
