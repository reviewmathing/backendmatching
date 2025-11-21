package com.hunko.missionmatching.core.presentation.dto;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.core.domain.ReviewRequestType;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public record ReviewRequestResponseDto(
        Long reviewRequestId,
        Long missionId,
        String missionName,
        ZonedDateTime startDateTime,
        ZonedDateTime endDateTime,
        Integer reviewCount,
        String githubUri,
        ReviewRequestType reviewRequestStatus
) {
    public static ReviewRequestResponseDto of(ReviewRequest reviewRequest, List<Mission> missions) {
        MissionInfo mission = missions.stream().filter(m -> m.getId().equals(reviewRequest.getMissionId()))
                .map(MissionInfo::of)
                .findFirst().orElseGet(()->MissionInfo.empty());
        return new ReviewRequestResponseDto(
                reviewRequest.getReviewRequestId().toLong(),
                reviewRequest.getMissionId().toLong(),
                mission.name,
                mission.startDateTime,
                mission.endDateTime,
                reviewRequest.getReviewCount(),
                reviewRequest.getGithubUri() == null ? null : reviewRequest.getGithubUri().toUriString(),
                reviewRequest.getReviewRequestStatus()
        );
    }

    private static class MissionInfo {
        private Long id;
        private String name;
        private ZonedDateTime startDateTime;
        private ZonedDateTime endDateTime;

        public static MissionInfo of(Mission mission) {
            MissionInfo missionInfo = new MissionInfo();
            missionInfo.id = mission.getId().toLong();
            missionInfo.name = mission.getTitle();
            missionInfo.startDateTime = mission.getTimePeriod().getStartDate();
            missionInfo.endDateTime = mission.getTimePeriod().getEndDate();
            return missionInfo;
        }

        public static MissionInfo empty() {
            MissionInfo missionInfo = new MissionInfo();
            missionInfo.id = -1L;
            missionInfo.name = "Unknown";
            missionInfo.startDateTime = null;
            missionInfo.endDateTime = null;
            return missionInfo;
        }
    }
}
