package com.hunko.missionmatching.core.domain;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingService {

    public List<ReviewAssignment> match(MissionId missionId, ZonedDateTime limitTime,
                                        List<ReviewRequest> reviewRequests) {
        Map<Integer, ReviewRequest> idMap = new HashMap<>();
        int[] reviewCounts = new int[reviewRequests.size()];
        for (int i = 0; i < reviewRequests.size(); i++) {
            idMap.put(i, reviewRequests.get(i));
            reviewCounts[i] = reviewRequests.get(i).getReviewCount();
        }

        MatchingAlgorithm matchingAlgorithm = new MatchingAlgorithm(reviewCounts);
        List<List<Integer>> process = matchingAlgorithm.process();

        List<ReviewAssignment> result = new ArrayList<>();
        for (int i = 0; i < reviewRequests.size(); i++) {
            ReviewRequest request = idMap.get(i);
            List<Integer> revieweeIdx = process.get(i);
            List<Reviewee> reviewees = new ArrayList<>();
            for (Integer idx : revieweeIdx) {
                ReviewRequest request1 = idMap.get(idx);
                reviewees.add(new Reviewee(
                        RevieweeId.of(request1.getRequester().id())
                ));
            }

            result.add(new ReviewAssignment(
                    missionId,
                    ReviewerId.of(request.getRequester().id()),
                    limitTime,
                    reviewees
            ));
        }
        for (ReviewRequest reviewRequest : reviewRequests) {
            DomainEventPublisher.instance().published(new ReviewMatched(
                    reviewRequest.getReviewRequestId()
            ));
        }
        return result;
    }
}
