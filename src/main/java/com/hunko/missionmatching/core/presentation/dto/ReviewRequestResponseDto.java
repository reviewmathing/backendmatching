package com.hunko.missionmatching.core.presentation.dto;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.domain.ReviewRequestType;
import java.util.List;

public record ReviewRequestResponseDto(
        Long reviewRequestId,
        Long missionId,
        String missionName,
        String githubUri,
        ReviewRequestType reviewRequestStatus
) {
    public static ReviewRequestResponseDto of(ReviewRequest reviewRequest, List<Mission> missions) {
        String name = missions.stream().filter(m -> m.getId().equals(reviewRequest.getMissionId()))
                .map(Mission::getTitle)
                .findFirst().orElse("Unknow");
        return new ReviewRequestResponseDto(
                reviewRequest.getReviewRequestId().toLong(),
                reviewRequest.getMissionId().toLong(),
                name,
                reviewRequest.getGithubUri() == null ? null : reviewRequest.getGithubUri().toUriString(),
                reviewRequest.getReviewRequestStatus()
        );
    }
}
