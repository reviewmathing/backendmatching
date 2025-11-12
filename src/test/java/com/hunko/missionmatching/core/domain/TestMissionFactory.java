package com.hunko.missionmatching.core.domain;

import java.time.LocalDateTime;

public class TestMissionFactory {

    private TestMissionFactory() {
    }

    public static Mission createMission(Long id, MissionStatus missionStatus, Long creator) {
        LocalDateTime now = LocalDateTime.now();
        switch (missionStatus) {
            case PENDING: {
                LocalDateTime endDateTime = now.minusDays(1);
                LocalDateTime startTime = endDateTime.minusDays(1);
                return createMission(id, missionStatus, creator, startTime, endDateTime);
            }
            case COMPLETED: {
                LocalDateTime endDateTime = now.plusDays(2);
                LocalDateTime startTime = endDateTime.minusDays(1);
                return createMission(id, missionStatus, creator, startTime, endDateTime);
            }
            default: {
                LocalDateTime endDateTime = now.plusDays(1);
                LocalDateTime startTime = now.minusDays(1);
                return createMission(id, missionStatus, creator, startTime, endDateTime);
            }
        }
    }

    private static Mission createMission(Long id, MissionStatus missionStatus, Long creator, LocalDateTime startTime,
                                         LocalDateTime endDateTime) {
        return new Mission(
                id,
                "test" + (id == null ? "" : id.toString()),
                new TimePeriod(startTime, endDateTime),
                Creator.of(creator),
                missionStatus
        );
    }
}
