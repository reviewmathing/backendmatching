package com.hunko.missionmatching.helper;

import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.domain.ReviewRequestId;
import com.hunko.missionmatching.core.domain.ReviewRequestSaver;
import java.util.ArrayList;
import java.util.List;

public class FakeReviewRequestSaver extends ReviewRequestSaver {

    private final List<ReviewRequest> requests = new ArrayList<>();

    public FakeReviewRequestSaver() {
        super(null);
    }

    @Override
    public Long save(ReviewRequest request) {
        ReviewRequest request1 = cotyAntSetId(request);
        requests.add(request1);
        return request1.getReviewRequestId().id();
    }

    private ReviewRequest cotyAntSetId(ReviewRequest request) {
        int id = requests.size();
        return new ReviewRequest(
                request.getReviewRequestId().id() == null ? new ReviewRequestId(Long.valueOf(id))
                        : request.getReviewRequestId(),
                request.getRequester(),
                request.getMissionId(),
                request.getReviewCount(),
                null,
                request.getReviewRequestStatus()
        );
    }

    public ReviewRequest getRequest(ReviewRequestId requestId) {
        return requests.stream().filter(request -> request.getReviewRequestId().equals(requestId)).findFirst()
                .orElse(null);
    }
}
