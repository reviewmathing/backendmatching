package com.hunko.missionmatching.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hunko.missionmatching.helper.even.DomainEventUnitTest;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class MatchingServiceTest extends DomainEventUnitTest {

    @Test
    public void 리뷰매칭() {
        MissionId missionId = MissionId.of(1L);
        List<ReviewRequest> reviewRequests = new ArrayList<>();
        reviewRequests.add(new ReviewRequest(
                ReviewRequestId.of(1L),
                Requester.of(1L),
                missionId,
                1,
                null,
                ReviewRequestType.REQUEST
        ));
        reviewRequests.add(new ReviewRequest(
                ReviewRequestId.of(2L),
                Requester.of(2L),
                missionId,
                1,
                null,
                ReviewRequestType.REQUEST
        ));
        reviewRequests.add(new ReviewRequest(
                ReviewRequestId.of(3L),
                Requester.of(3L),
                missionId,
                2,
                null,
                ReviewRequestType.REQUEST
        ));
        MatchingService matchingService = new MatchingService();
        ZonedDateTime limitTime = ZonedDateTime.now();

        List<ReviewAssignment> match = matchingService.match(missionId, limitTime, reviewRequests);

        assertThat(match).containsAnyOf(
                new ReviewAssignment(
                        missionId,
                        ReviewerId.of(1L),
                        limitTime,
                        List.of(
                                new Reviewee(RevieweeId.of(3L))
                        )
                ),
                new ReviewAssignment(
                        missionId,
                        ReviewerId.of(2L),
                        limitTime,
                        List.of(
                                new Reviewee(RevieweeId.of(3L))
                        )
                ),
                new ReviewAssignment(
                        missionId,
                        ReviewerId.of(3L),
                        limitTime,
                        List.of(
                                new Reviewee(RevieweeId.of(1L)),
                                new Reviewee(RevieweeId.of(2L))
                        )
                )
        );
        List<ReviewMatched> events = getEvents(ReviewMatched.class);
        assertThat(events).containsAnyOf(
                new ReviewMatched(
                        ReviewRequestId.of(1L)
                ),
                new ReviewMatched(
                        ReviewRequestId.of(2L)
                ),
                new ReviewMatched(
                        ReviewRequestId.of(3L)
                )
        );
    }
}