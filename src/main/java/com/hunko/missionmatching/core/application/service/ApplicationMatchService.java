package com.hunko.missionmatching.core.application.service;

import com.hunko.missionmatching.core.domain.MatchingService;
import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.MissionReader;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewLimitTimeCalcService;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationMatchService {

    private final ReviewAssignmentSaver reviewAssignmentSaver;
    private final ReviewLimitTimeCalcService reviewLimitTimeCalcService;

    @Transactional
    public void match(Mission mission, List<ReviewRequest> reviewRequests) {
        MatchingService matchingService = new MatchingService();
        ZonedDateTime limitTime = reviewLimitTimeCalcService.calc(mission.getTimePeriod().getEndDate());
        List<ReviewAssignment> match = matchingService.match(mission.getId(), limitTime, reviewRequests);
        reviewAssignmentSaver.save(match);
    }
}
