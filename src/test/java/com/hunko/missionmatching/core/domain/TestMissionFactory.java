package com.hunko.missionmatching.core.domain;

import java.time.ZonedDateTime;

public class TestMissionFactory {

    private static final GithubUri DEFAULT_GITHUB_URL = GithubUri.of(
            "https://github.com/woowacourse-precourse/java-lotto-8");

    private TestMissionFactory() {
    }

    public static Mission createMission(Long id, MissionStatus missionStatus, Long creator) {
        return createMission(id, missionStatus, creator, DEFAULT_GITHUB_URL);
    }

    public static Mission createMission(Long id, MissionStatus missionStatus, Long creator, GithubUri githubUri) {
        ZonedDateTime now = ZonedDateTime.now();
        switch (missionStatus) {
            case PENDING: {
                ZonedDateTime endDateTime = now.minusDays(1);
                ZonedDateTime startTime = endDateTime.minusDays(1);
                return createMission(id, missionStatus, creator, startTime, endDateTime, githubUri);
            }
            case COMPLETED: {
                ZonedDateTime endDateTime = now.plusDays(2);
                ZonedDateTime startTime = endDateTime.minusDays(1);
                return createMission(id, missionStatus, creator, startTime, endDateTime, githubUri);
            }
            default: {
                ZonedDateTime endDateTime = now.plusDays(1);
                ZonedDateTime startTime = now.minusDays(1);
                return createMission(id, missionStatus, creator, startTime, endDateTime, githubUri);
            }
        }
    }

    private static Mission createMission(Long id, MissionStatus missionStatus, Long creator, ZonedDateTime startTime,
                                         ZonedDateTime endDateTime, GithubUri missionUri) {
        return new Mission(
                MissionId.of(id),
                "test" + (id == null ? "" : id.toString()),
                new TimePeriod(startTime, endDateTime),
                Creator.of(creator),
                missionStatus,
                missionUri
        );
    }
}
