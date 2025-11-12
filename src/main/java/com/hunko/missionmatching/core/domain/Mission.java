package com.hunko.missionmatching.core.domain;

import com.hunko.missionmatching.core.exception.ErrorType;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Mission {

    private final Long id;
    private final String title;
    private final TimePeriod timePeriod;
    private final Creator creator;
    private MissionStatus status;


    public Mission(String title, TimePeriod timePeriod, Creator creator) {
        this(null, title, timePeriod, creator, MissionStatus.PENDING);
    }

    public Mission(Long id, String title, TimePeriod timePeriod, Creator creator, MissionStatus status) {
        if (title == null || title.isEmpty() || creator == null || timePeriod == null) {
            ErrorType.INVALID_INPUT.throwException();
        }
        this.id = id;
        this.title = title;
        this.timePeriod = timePeriod;
        this.creator = creator;
        this.status = status;
    }

    public void updateOngoing(LocalDateTime startedTime) {
        if (!MissionStatus.PENDING.equals(this.status) || !this.timePeriod.isEqualsStartDate(startedTime)) {
            //todo : 추후 업데이트 예정
            ErrorType.INVALID_INPUT.throwException();
        }
        this.status = MissionStatus.ONGOING;
        DomainEventPublisher.instance().published(new MissionOngoinged(this.id));
    }

    public void updateCompleted(LocalDateTime endDate) {
        if (!MissionStatus.ONGOING.equals(this.status) || !this.timePeriod.isEqualsEndDate(endDate)) {
            //todo : 추후 업데이트 예정
            ErrorType.INVALID_INPUT.throwException();
        }
        this.status = MissionStatus.COMPLETED;
        DomainEventPublisher.instance().published(new MissionCompleted(this.id));
    }
}
