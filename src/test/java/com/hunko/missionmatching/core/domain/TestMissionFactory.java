package com.hunko.missionmatching.core.domain;

import java.time.ZonedDateTime;

public class TestMissionFactory {

    private TestMissionFactory() {
    }

    public static Mission createMission(Long id, MissionStatus missionStatus, Long creator) {
        ZonedDateTime now = ZonedDateTime.now();
        switch (missionStatus) {
            case PENDING: {
                ZonedDateTime endDateTime = now.minusDays(1);
                ZonedDateTime startTime = endDateTime.minusDays(1);
                return createMission(id, missionStatus, creator, startTime, endDateTime);
            }
            case COMPLETED: {
                ZonedDateTime endDateTime = now.plusDays(2);
                ZonedDateTime startTime = endDateTime.minusDays(1);
                return createMission(id, missionStatus, creator, startTime, endDateTime);
            }
            default: {
                ZonedDateTime endDateTime = now.plusDays(1);
                ZonedDateTime startTime = now.minusDays(1);
                return createMission(id, missionStatus, creator, startTime, endDateTime);
            }
        }
    }

    private static Mission createMission(Long id, MissionStatus missionStatus, Long creator, ZonedDateTime startTime,
                                         ZonedDateTime endDateTime) {
        return new Mission(
                MissionId.of(id),
                "test" + (id == null ? "" : id.toString()),
                new TimePeriod(startTime, endDateTime),
                Creator.of(creator),
                missionStatus
        );
    }
}
