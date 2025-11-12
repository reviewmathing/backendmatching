package com.hunko.missionmatching.core.domain;

public enum MissionStatus {
    ONGOING("진행중"),
    PENDING("대기중"),
    COMPLETED("종료됨")
    ;

    private final String description;

    MissionStatus(String description) {
        this.description = description;
    }
}
