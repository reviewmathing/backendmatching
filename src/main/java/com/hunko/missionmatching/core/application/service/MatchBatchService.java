package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.ExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchBatchService {

    private final EntityManager entityManager;
    private final ApplicationMatchService applicationMatchService;
    private final ExecutorService batchMatchExcutorService;

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public void match(Mission mission) {
        ReviewRequestBatchReader reviewRequestBatchReader = new ReviewRequestBatchReader(entityManager, 50, 10,
                mission.getId());
        while (true) {
            List<ReviewRequest> reviewRequests = reviewRequestBatchReader.reviewRequests();
            if (reviewRequests.isEmpty()) {
                break;
            }
            batchMatchExcutorService.execute(() -> {
                applicationMatchService.match(mission, reviewRequests);
            });
        }
    }
}
